package com.cst8277.usermanagementservice;

public class Token {
    private String userId;
    private long token;

    public Token(String userId) {
        this.userId = userId;
        this.token = generateToken();
    }

    private long generateToken() {
        return System.currentTimeMillis() + userId.hashCode();  // Example of numeric token
    }

    public String getUserId() {
        return userId;
    }

    public long getToken() {
        return token;
    }
}
