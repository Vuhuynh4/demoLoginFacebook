package com.mservice.demologinfacebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {
    private String accessToken;
    private String tokenType;
    private int expiredIn;

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("expires_in")
    public int getExpiredIn() {
        return expiredIn;
    }
}
