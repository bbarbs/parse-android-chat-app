package com.avocado.chatapp.navigator;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.avocado.chatapp.ui.chat.ChatActivity;
import com.avocado.chatapp.ui.login.LoginActivity;
import com.avocado.chatapp.ui.main.MainActivity;
import com.avocado.chatapp.ui.signup.SignUpActivity;

public final class Navigator {

    private Navigator() {
    }

    public static void navigateToLoginActivity(@NonNull Context context) {
        Intent intent = LoginActivity.getIntent(context);
        context.startActivity(intent);
    }

    public static void navigateToChatActivity(@NonNull Context context) {
        Intent intent = ChatActivity.getIntent(context);
        context.startActivity(intent);
    }

    public static void navigateToMainActivity(@NonNull Context context) {
        Intent intent = MainActivity.getIntent(context);
        context.startActivity(intent);
    }

    public static void navigateToSignUpActivity(@NonNull Context context) {
        Intent intent = SignUpActivity.getIntent(context);
        context.startActivity(intent);
    }

}
