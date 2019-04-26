/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook.controller;

import com.mservice.demologinfacebook.util.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author vuhuynh
 */
@Controller
public class LoginController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    Environment env;

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
        return "index";
    }
}
