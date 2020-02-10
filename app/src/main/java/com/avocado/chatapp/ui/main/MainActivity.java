package com.avocado.chatapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.avocado.chatapp.R;
import com.avocado.chatapp.navigator.Navigator;
import com.parse.ParseUser;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoggedUserAndRedirectToChatIfExist();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideActionBar();
    }

    private void checkLoggedUserAndRedirectToChatIfExist() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Navigator.navigateToChatActivity(this);
            finish();
        }
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @OnClick(R.id.button_main_signup)
    void onClickSignUpButton() {
        Navigator.navigateToSignUpActivity(this);
    }

    @OnClick(R.id.button_main_login)
    void onClickLoginButton() {
        Navigator.navigateToLoginActivity(this);
    }
}
