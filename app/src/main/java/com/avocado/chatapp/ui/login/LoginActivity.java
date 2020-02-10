package com.avocado.chatapp.ui.login;

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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.toolbar_login)
    Toolbar toolbar;

    @BindView(R.id.textview_login_signup)
    TextView signUpTextView;

    @BindView(R.id.textview_login_termsofservice)
    TextView termsOfServiceTextView;

    @BindView(R.id.textinputlayout_login_username)
    TextInputLayout usernameTextInputLayout;

    @BindView(R.id.textinputedittext_login_username)
    TextInputEditText usernameTextInputEditText;

    @BindView(R.id.textinputlayout_login_password)
    TextInputLayout passwordTextInputLayout;

    @BindView(R.id.textinputedittext_login_password)
    TextInputEditText passwordTextInputEditText;

    @BindView(R.id.progressbar_main)
    ProgressBar mainProgressBar;

    @BindView(R.id.linearlayout_login_background)
    LinearLayout loginBackgroundLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setToolbar();
        setTitle(null);
        setTermsOfServiceAndPrivacyPolicy();
        addUnderlineInSignUpLink();
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

    private void addUnderlineInSignUpLink() {
        signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void prepareForLogin(String username, String password) {
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

        boolean isUsernameValid = usernameValidator.check();
        boolean isPasswordValid = passwordValidator.check();

        if (isUsernameValid && isPasswordValid) {
            KeyboardUtil.hideKeyboard(this);
            setMainProgressBarVisibility(View.VISIBLE);
            setLoginBackgroundLinearLayoutVisibility(View.GONE);

            loginUser(username, password);
        }
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> runOnUiThread(() -> {
            if (user != null) {
                initializeOnSuccessLoginUser();
            } else {
                initializeOnErrorLoginUser(e);
            }
            initializeOnFinishLoginUser();
        }));
    }

    private void initializeOnFinishLoginUser() {
        setMainProgressBarVisibility(View.GONE);
        setLoginBackgroundLinearLayoutVisibility(View.VISIBLE);
    }

    private void initializeOnSuccessLoginUser() {
        Navigator.navigateToChatActivity(this);
        finish();
    }

    private void initializeOnErrorLoginUser(ParseException e) {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        showErrorMessageDuringLogin();
    }

    private void showErrorMessageDuringLogin() {
        usernameTextInputLayout.setError(getString(R.string.all_value_is_incorrect));
        passwordTextInputLayout.setError(getString(R.string.all_value_is_incorrect));
    }

    private void setMainProgressBarVisibility(int visibility) {
        mainProgressBar.setVisibility(visibility);
    }

    private void setLoginBackgroundLinearLayoutVisibility(int visibility) {
        loginBackgroundLinearLayout.setVisibility(visibility);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @OnTextChanged(R.id.textinputedittext_login_username)
    void onUsernameTextChanged() {
        usernameTextInputLayout.setError(null);
    }

    @OnTextChanged(R.id.textinputedittext_login_password)
    void onPasswordTextChanged() {
        passwordTextInputLayout.setError(null);
    }

    @OnClick(R.id.button_login)
    void onClickLoginButton() {
        String username = Objects.requireNonNull(usernameTextInputEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();
        prepareForLogin(username, password);
    }

    @OnClick(R.id.textview_login_signup)
    void onClickSignUpLink() {
        Navigator.navigateToSignUpActivity(this);
        finish();
    }
}
