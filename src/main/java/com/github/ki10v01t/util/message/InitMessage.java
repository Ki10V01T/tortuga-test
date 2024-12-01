package com.github.ki10v01t.util.message;

public class InitMessage extends Message{
    private String sessionToken;

    public InitMessage() {}
    
    public InitMessage(String text) {
        this.setText(text);
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
