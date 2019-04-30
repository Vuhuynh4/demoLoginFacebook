/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook;

import com.mservice.demologinfacebook.cache.CacheManager;
import com.mservice.demologinfacebook.client.LoginByFacebookClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vuhuynh
 */
@SpringBootApplication
public class Starter {
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Starter.class, args);
        CacheManager.init();
        LoginByFacebookClient.init();
    }
    
}
