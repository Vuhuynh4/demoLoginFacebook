package com.mservice.demologinfacebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserInfo extends BaseInfo{
    private String userId;
    private String userName;
    private String accessToken;
    private String longAccessToken;
    private String userProfilePic;
    private long expiredTime;
    private String userPageAccessName;
    private List<String> userPermission;

    public UserInfo() {
    }

    public UserInfo(String userId, String userName, String accessToken, String longAccessToken, String userProfilePic, long expiredTime, List<String> userPermission) {
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
        this.longAccessToken = longAccessToken;
        this.userProfilePic = userProfilePic;
        this.expiredTime = expiredTime;
        this.userPermission = userPermission;
    }

    public String getUserId() {
        return super.getId();
    }

    public String getUserName() {
        return super.getName();
    }

    public String getAccessToken() {
        return super.getAccessToken();
    }

    @JsonProperty("long_access_token")
    public String getLongAccessToken() {
        return longAccessToken;
    }

    public void setLongAccessToken(String longAccessToken) {
        this.longAccessToken = longAccessToken;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    @JsonProperty("expired_time")
    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @JsonProperty("page_access_name")
    public String getUserPageAccessName() {
        return userPageAccessName;
    }

    public void setUserPageAccessName(String userPageAccessName) {
        this.userPageAccessName = userPageAccessName;
    }

    @JsonProperty("permission")
    public List<String> getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(List<String> userPermission) {
        this.userPermission = userPermission;
    }
}
