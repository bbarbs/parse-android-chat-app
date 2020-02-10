package com.avocado.chatapp.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.avocado.chatapp.R;
import com.avocado.chatapp.navigator.Navigator;
import com.avocado.chatapp.util.AssetUtil;
import com.avocado.chatapp.util.InputValidatorUtil;
import com.avocado.chatapp.util.KeyboardUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.wajahatkarim3.easyvalidation.core.Validator;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import kotlin.Unit;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.toolbar_signup)
    Toolbar toolbar;

    @BindView(R.id.textview_signup_login)
    TextView loginTextView;

    @BindView(R.id.textview_signup_termsofservice)
    TextView termsOfServiceTextView;

    @BindView(R.id.textinputedittext_signup_username)
    TextInputEditText usernameTextInputEditText;

    @BindView(R.id.textinputedittext_signup_password)
    TextInputEditText passwordTextInputEditText;

    @BindView(R.id.textinputlayout_signup_username)
    TextInputLayout usernameTextInputLayout;

    @BindView(R.id.textinputlayout_signup_password)
    TextInputLayout passwordTextInputLayout;

    @BindView(R.id.progressbar_main)
    ProgressBar mainProgressBar;

    @BindView(R.id.linearlayout_signup_background)
    LinearLayout signUpBackgroundLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setToolbar();
        setTitle(null);
        setTermsOfServiceAndPrivacyPolicy();
        addUnderlineInLoginLink();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    private void setTermsOfServiceAndPrivacyPolicy() {
        String text = null;
        try {
            text = AssetUtil.readTextFileFromAsset(this, "terms_of_service_and_privacy_policy.txt");
        } catch (Exception e) {
            Log.e(TAG, "setTermsOfServiceAndPrivacyPolicy: ", e);
        }
        termsOfServiceTextView.setText(text);
    }

    private void addUnderlineInLoginLink() {
        loginTextView.setPaintFlags(loginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void prepareForSignUp(String username, String password) {
        Validator usernameValidator = InputValidatorUtil.inputValidator(username)
                .addErrorCallback(s -> {
                    usernameTextInputLayout.setError(getString(R.string.all_value_is_incorrect));
                    return Unit.INSTANCE;
                });
        Validator passwordValidator = InputValidatorUtil.inputValidator(password)
                .addErrorCallback(s -> {
                    passwordTextInputLayout.setError(getString(R.string.all_value_is_incorrect));
                    return Unit.INSTANCE;
                });

        boolean isValidUsername = usernameValidator.check();
        boolean isValidPassword = passwordValidator.check();

        if (isValidUsername && isValidPassword) {
            KeyboardUtil.hideKeyboard(this);
            setMainProgressBarVisibility(View.VISIBLE);
            setSignUpBackgroundLinearLayoutVisibility(View.GONE);

            ParseUser user = new ParseUser();
            constructUser(user);
            signUpUser(user);
        }
    }

    private void constructUser(ParseUser user) {
        String username = Objects.requireNonNull(usernameTextInputEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();

        String email = username + "@gmail.com"; // Dummy mail since empty is not allowed.
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
    }

    private void signUpUser(ParseUser user) {
        user.signUpInBackground(e -> runOnUiThread(() -> {
            if (e == null) {
                initializeOnSuccessSignUpUser();
            } else {
                initializeOnErrorSignUpUser(e);
            }
            initializeOnFinishSignUpUser();
        }));
    }

    private void initializeOnFinishSignUpUser() {
        setMainProgressBarVisibility(View.GONE);
        setSignUpBackgroundLinearLayoutVisibility(View.VISIBLE);
    }

    private void initializeOnSuccessSignUpUser() {
        Navigator.navigateToChatActivity(SignUpActivity.this);
        finish();
    }

    private void initializeOnErrorSignUpUser(ParseException e) {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setMainProgressBarVisibility(int visibility) {
        mainProgressBar.setVisibility(visibility);
    }

    private void setSignUpBackgroundLinearLayoutVisibility(int visibility) {
        signUpBackgroundLinearLayout.setVisibility(visibility);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @OnTextChanged(R.id.textinputedittext_signup_username)
    void onUsernameTextChanged() {
        usernameTextInputLayout.setError(null);
    }

    @OnTextChanged(R.id.textinputedittext_signup_password)
    void onPasswordTextChanged() {
        passwordTextInputLayout.setError(null);
    }

    @OnClick(R.id.textview_signup_login)
    void onClickLoginLink() {
        Navigator.navigateToLoginActivity(this);
        finish();
    }

    @OnClick(R.id.button_signup)
    void onClickButtonSignUp() {
        String username = Objects.requireNonNull(usernameTextInputEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();
        prepareForSignUp(username, password);
    }
}
