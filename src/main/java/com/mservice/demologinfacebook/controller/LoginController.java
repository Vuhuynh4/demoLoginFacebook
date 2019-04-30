/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook.controller;

import com.mservice.demologinfacebook.client.LoginByFacebookClient;
import com.mservice.demologinfacebook.model.AccessToken;
import com.mservice.demologinfacebook.model.UserInfo;
import com.mservice.demologinfacebook.services.LoginServices;
import com.mservice.demologinfacebook.util.Constants;
import com.mservice.demologinfacebook.util.Device;
import com.mservice.demologinfacebook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vuhuynh
 */
@Controller
public class LoginController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    Environment env;
    @Autowired
    LoginServices loginServices;

    LoginController(Environment env) {
        this.env = env;
    }

    @GetMapping(path="/")
    public String authentication(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(name = "Proxy-Client-IP", required = false) String proxyClientIP,
            @RequestHeader(name = "source", required = false) String source,
            @RequestHeader(name = "X-Requested-With", required = false) String xRequestedWith,
            @RequestHeader(name = "appVersion", required = false) String appVersion,
            @RequestHeader(name = "platform", required = false) String platform,
            Model model) {
        Device device = new Device(userAgent);
        device.getAllInformation("" + System.currentTimeMillis() + Math.random() * 100);
        return "index_backup";
    }

    @PostMapping(path="/login")
    public String loginSuccess(
            @RequestParam(required = false, value = "code") String code,
            @RequestParam(required = false, value = "id") String accessToken,
            Model model) {
        LOGGER.info("id " + accessToken);
        LOGGER.info("code " + code);
        try {

            if (Utils.isEmpty(accessToken)&& Utils.isEmpty(code)) {
                LOGGER.info("There is something wrong");
                return "error";
            }
            UserInfo userInfo = new UserInfo();
            if (!Utils.isEmpty(code)) {
                LOGGER.info("GET BY CODE: " + userInfo.toString());
                LOGGER.info("DO GET ACCESS TOKEN");
                Map<String, Object> getAccessToken = new HashMap<>();
                getAccessToken.put("client_id",env.getProperty(Constants.FB_OAUTH_CLIENT_ID_KEY));
                getAccessToken.put("redirect_uri", env.getProperty(Constants.FB_OAUTH_REDIRECT_URI_KEY));
                getAccessToken.put("client_secret",env.getProperty(Constants.FB_OAUTH_CLIENT_SECRET_KEY));
                getAccessToken.put("client_secret",code);

                String getAccessTokenResponse = LoginByFacebookClient.getInstance().doCallGet(Utils.doCreateUrlWithParams(env.getProperty(Constants.FB_OAUTH_TOKEN_URL_KEY), getAccessToken));
                LOGGER.info("Response: " + getAccessTokenResponse);
                AccessToken accessTokenResponse = Utils.fromString(getAccessTokenResponse, AccessToken.class);
                userInfo = loginServices.loginByFb(accessTokenResponse.getAccessToken());
            }
            if (!Utils.isEmpty(accessToken)) {
                LOGGER.info("GET BY ACCESS TOKEN: " + userInfo.toString());
                userInfo = loginServices.loginByFb(accessToken);

            }
            model.addAttribute("userInfo", userInfo);
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return "success_backup";
    }
}
