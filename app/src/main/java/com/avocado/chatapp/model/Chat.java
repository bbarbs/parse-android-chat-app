package com.avocado.chatapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Chat")
public class Chat extends ParseObject {

    public static final String USERNAME = "username";
    public static final String MESSAGE = "message";

    public String getUsername() {
        return getString(USERNAME);
    }

    public void setUsername(String username) {
        put(USERNAME, username);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String message) {
        put(MESSAGE, message);
    }
}
