package com.mservice.demologinfacebook.client;

import com.mservice.demologinfacebook.util.Utils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class LoginByFacebookClient {

    private static LoginByFacebookClient instance;
    private CloseableHttpClient client;

    private LoginByFacebookClient() {
        client = HttpClients.createDefault();
    }

    public static void init() {
        instance = new LoginByFacebookClient();
    }

    public static LoginByFacebookClient getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    public String doCallGet(String url) throws Exception{
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse result = client.execute(request);
        return EntityUtils.toString(result.getEntity());
    }





}
