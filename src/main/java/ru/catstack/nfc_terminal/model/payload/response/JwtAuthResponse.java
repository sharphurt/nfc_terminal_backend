package ru.catstack.nfc_terminal.model.payload.response;

public class JwtAuthResponse {
    private final String token;
    private final String tokenType;

    public JwtAuthResponse(String token, String tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
