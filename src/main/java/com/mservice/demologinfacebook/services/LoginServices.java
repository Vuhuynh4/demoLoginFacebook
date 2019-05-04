package com.mservice.demologinfacebook.services;

import com.mservice.demologinfacebook.cache.CacheManager;
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

            UserInfo cacheUserInfo = (UserInfo) CacheManager.getInstance().getCache(userInfo.getUserId());
            if (cacheUserInfo != null) {
                LOGGER.info("[" + userInfo.getUserId() + "] DO GET USER INFO IN CACHE: " + Utils.toString(cacheUserInfo));

                    cacheUserInfo.getPageInfos().stream().forEach(item -> {
                        try {
                            callFbToLinkWebHookToPage(item);
                        }catch(Exception ex) {
                            LOGGER.error(ex.getMessage());
                        }
                    });

                return cacheUserInfo;
            }
            userInfo.setAccessToken(accessToken);
            String longLivedAccessTokenResponse = callFbToGetLongAccessToken(accessToken, userInfo);
            LongLiveAccessToken longLiveAccessToken = Utils.fromString(longLivedAccessTokenResponse, LongLiveAccessToken.class);
            userInfo.setLongAccessToken(longLiveAccessToken.getAccessToken());

            callFbToGetPageInfoOfUser(userInfo);
            LOGGER.info("[" + userInfo.getUserId() + "] ADD USER INFO CACHE " + Utils.toString(userInfo));
            CacheManager.getInstance().addCache(userInfo.getUserId(), userInfo.getUserId(), 3600000, userInfo);
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
            userInfo.setPageInfo(item);
            callFbToLinkWebHookToPage(item);
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

    private String callFbToLinkWebHookToPage(PageInfo pageInfo) throws Exception {
        LOGGER.info("DO CALL FB TO LINK WEBHOOK TO USER PAGE: ["+pageInfo.getId()+"] - ["+pageInfo.getName()+"]");

        String url = "https://graph.facebook.com/v3.3/"+
                     pageInfo.getId()+
                     "/subscribed_apps?subscribed_fields=%20messages&access_token="+
                    pageInfo.getAccessToken();

        String result = LoginByFacebookClient.getInstance().doCallPost(url);
        LOGGER.info("Response: " + result);
        return result;
    }

}


/***
 *
 * curl -i -X POST \
 *  "https://graph.facebook.com/v3.3/1230295813763775/subscribed_apps?subscribed_fields=%20messages&access_token=EAAQ6gnw49tUBAAD59wZAgQTPrhZC01kMS6Qn09srIQRxfKrFZABVZCKhmWmNtAHtRqFcnwILc3GZBg4uDOY444yMfkad5qggC4GD6yZCZCBjzRHuLSjSVXOmyojf1hFkqROdftjbPTMJZClZCQ1D4Fi1CpusrxGaZAmgrwD7nHpvsd1q75MDG2NEzZBn4r8CXPCAecZD"
 *
 */
