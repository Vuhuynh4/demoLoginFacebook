package com.mservice.demologinfacebook.services;

import com.mservice.demologinfacebook.client.LoginByFacebookClient;
import com.mservice.demologinfacebook.model.LongLiveAccessToken;
import com.mservice.demologinfacebook.model.PageInfo;
import com.mservice.demologinfacebook.model.PageLists;
import com.mservice.demologinfacebook.model.UserInfo;
import com.mservice.demologinfacebook.util.Constants;
import com.mservice.demologinfacebook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServices.class);
    Environment env;

    public LoginServices(Environment env) {
        this.env = env;
    }

    public UserInfo loginByFb(String accessToken) {
        UserInfo userInfo = new UserInfo();
        try {
            String userNameResponse = callFbToGetUserName(accessToken);
            userInfo = Utils.fromString(userNameResponse, UserInfo.class);
            userInfo.setAccessToken(accessToken);
            String longLivedAccessTokenResponse = callFbToGetLongAccessToken(accessToken, userInfo);
            LongLiveAccessToken longLiveAccessToken = Utils.fromString(longLivedAccessTokenResponse, LongLiveAccessToken.class);
            userInfo.setLongAccessToken(longLiveAccessToken.getAccessToken());

            callFbToGetPageInfoOfUser(userInfo);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return userInfo;
    }

    private void callFbToGetPageInfoOfUser(UserInfo userInfo) throws Exception {
        LOGGER.info("[" + userInfo.getUserId() + "] DO GET PAGE ACCESS INFO");
        Map<String, Object> getPageAccessInfo = new HashMap<>();
        getPageAccessInfo.put("access_token", userInfo.getLongAccessToken());
        String pageInfoResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty(Constants.FB_OAUTH_APP_PAGE_URL_KEY), getPageAccessInfo));
        LOGGER.info("[" + userInfo.getUserId() + "] Response: " + pageInfoResponse);
        PageLists pageLists = Utils.fromString(pageInfoResponse, PageLists.class);
        for (PageInfo item: pageLists.getData()) {
            //if(item.getId().equals(env.getProperty(Constants.FB_OAUTH_PAGE_ID_KEY))){
                userInfo.setPageInfo(item);
            //}
        }
    }

    private String callFbToGetLongAccessToken(String accessToken, UserInfo userInfo) throws Exception {
        LOGGER.info("[" + userInfo.getUserId() + "] DO GET LONG LIVE ACCESS TOKEN");
        Map<String, Object> getLongLivedAccessToken = new HashMap<>();
        getLongLivedAccessToken.put("grant_type",env.getProperty(Constants.FB_OAUTH_GRANT_TYPE_KEY));
        getLongLivedAccessToken.put("client_id",env.getProperty(Constants.FB_OAUTH_CLIENT_ID_KEY));
        getLongLivedAccessToken.put("client_secret",env.getProperty(Constants.FB_OAUTH_CLIENT_SECRET_KEY));
        getLongLivedAccessToken.put("fb_exchange_token",accessToken);

        String longLivedAccessTokenResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty(Constants.FB_OAUTH_LONG_TOKEN_URL_KEY), getLongLivedAccessToken));
        LOGGER.info("[" + userInfo.getUserId() + "] Response: " + longLivedAccessTokenResponse);
        return longLivedAccessTokenResponse;
    }

    private String callFbToGetUserName(String accessToken) throws Exception {
        LOGGER.info("DO GET USER NAME");
        Map<String, Object> getUserNameParams = new HashMap<>();
        getUserNameParams.put("access_token", accessToken);

        String userNameResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty(Constants.FB_OAUTH_ME_URL_KEY), getUserNameParams));
        LOGGER.info("Response: " + userNameResponse);
        return userNameResponse;
    }

}
