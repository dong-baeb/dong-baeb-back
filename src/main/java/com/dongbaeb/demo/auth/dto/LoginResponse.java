package com.dongbaeb.demo.auth.dto;

public record LoginResponse(
        String accessToken,
        boolean isRegistered
) {

    public static LoginResponse createRegistered(String accessToken) {
        return new LoginResponse(accessToken, true);
    }

    public static LoginResponse createNotRegistered() {
        return new LoginResponse(null, false);
    }
}
