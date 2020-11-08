package ru.catstack.nfc_terminal.model.payload.response;

public class JwtAuthResponse {
    private final String accessToken;
    private final String tokenType;

    public JwtAuthResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
