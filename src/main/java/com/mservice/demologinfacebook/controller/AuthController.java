package com.mservice.demologinfacebook.controller;

import com.mservice.demologinfacebook.client.LoginByFacebookClient;
import com.mservice.demologinfacebook.model.LongLiveAccessToken;
import com.mservice.demologinfacebook.model.PageInfo;
import com.mservice.demologinfacebook.model.PageLists;
import com.mservice.demologinfacebook.model.UserInfo;
import com.mservice.demologinfacebook.services.LoginServices;
import com.mservice.demologinfacebook.util.Constants;
import com.mservice.demologinfacebook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    Environment env;
    @Autowired
    LoginServices loginServices;

    AuthController(Environment env) {
        this.env = env;
    }

    @GetMapping(path="/login-success")
    public UserInfo loginSuccess(@RequestParam(value = "access_token") String accessToken) {
        UserInfo userInfo = loginServices.loginByFb(accessToken);
        return userInfo;
    }
}
