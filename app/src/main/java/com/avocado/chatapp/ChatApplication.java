package com.avocado.chatapp;

import android.app.Application;

import com.avocado.chatapp.config.ParseConfig;
import com.avocado.chatapp.model.Chat;
import com.parse.Parse;
import com.parse.ParseObject;

public class ChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerParseSubClasses();
        initializeParse();
    }

    private void registerParseSubClasses() {
        ParseObject.registerSubclass(Chat.class);
    }

    private void initializeParse() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(ParseConfig.APPLICATION_ID)
                .clientKey(ParseConfig.CLIENT_KEY)
                .server(ParseConfig.SERVER)
                .build()
        );
    }
}
