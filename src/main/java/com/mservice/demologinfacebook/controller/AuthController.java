package com.mservice.demologinfacebook.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mservice.demologinfacebook.client.LoginByFacebookClient;
import com.mservice.demologinfacebook.model.LongLiveAccessToken;
import com.mservice.demologinfacebook.model.PageInfo;
import com.mservice.demologinfacebook.model.PageLists;
import com.mservice.demologinfacebook.model.UserInfo;
import com.mservice.demologinfacebook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    Environment env;

    AuthController(Environment env) {
        this.env = env;
    }

    @GetMapping(path="/login-success")
    public UserInfo loginSuccess(@RequestParam(value = "access_token") String accessToken) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccessToken(accessToken);
        try {
            LOGGER.info("DO GET USER NAME");
            Map<String, Object> getUserNameParams = new HashMap<>();
            getUserNameParams.put("access_token", accessToken);

            String userNameResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty("fb.oauth.me.url"), getUserNameParams));
            LOGGER.info("Response: " + userNameResponse);
            userInfo = Utils.fromString(userNameResponse, UserInfo.class);

            LOGGER.info("[" + userInfo.getUserId() + "] DO GET LONG LIVE ACCESS TOKEN");
            Map<String, Object> getLongLivedAccessToken = new HashMap<>();
            getLongLivedAccessToken.put("grant_type",env.getProperty("fb.oauth.grant.type"));
            getLongLivedAccessToken.put("client_id",env.getProperty("fb.oauth.client.id"));
            getLongLivedAccessToken.put("client_secret",env.getProperty("fb.oauth.client.secret"));
            getLongLivedAccessToken.put("fb_exchange_token",accessToken);

            String longLivedAccessTokenResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty("fb.oauth.long.lived.url"), getLongLivedAccessToken));
            LOGGER.info("[" + userInfo.getUserId() + "] Response: " + longLivedAccessTokenResponse);
            LongLiveAccessToken longLiveAccessToken = Utils.fromString(longLivedAccessTokenResponse, LongLiveAccessToken.class);
            userInfo.setLongAccessToken(longLiveAccessToken.getAccessToken());

            LOGGER.info("[" + userInfo.getUserId() + "] DO GET PAGE ACCESS INFO");
            Map<String, Object> getPageAccessInfo = new HashMap<>();
            getPageAccessInfo.put("access_token", userInfo.getLongAccessToken());
            String pageInfoResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty("fb.oauth.app.page.access.url"), getPageAccessInfo));
            LOGGER.info("[" + userInfo.getUserId() + "] Response: " + pageInfoResponse);
            PageLists pageLists = Utils.fromString(pageInfoResponse, PageLists.class);
            for (PageInfo item: pageLists.getData()) {
                if(item.getId().equals(env.getProperty("fb.oauth.page.id"))){
                    userInfo.setUserPermission(item.getTasks());
                    userInfo.setUserPageAccessName(item.getName());
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return userInfo;
    }
}
