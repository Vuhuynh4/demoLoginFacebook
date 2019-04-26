package com.mservice.demologinfacebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LongLiveAccessToken {
    private String accessToken;
    private String tokenType;
    private long expiredTime;

    public LongLiveAccessToken() {
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("expires_in")
    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
