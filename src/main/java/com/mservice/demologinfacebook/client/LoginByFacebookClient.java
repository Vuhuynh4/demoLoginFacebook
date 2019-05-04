package com.mservice.demologinfacebook.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mservice.demologinfacebook.model.PageInfo;
import com.mservice.demologinfacebook.util.Utils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

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

    public String doCallPost(String url) throws Exception{
        RequestBuilder builder = RequestBuilder.post()
                .setUri(url);

        HttpUriRequest httpUriRequest = builder.build();

        CloseableHttpResponse result = client.execute(httpUriRequest);
        System.out.println("DO CALL FB WITH STATUS RESPONSE [" + result.getStatusLine().getStatusCode() + "]");
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            JsonNode responseObj = Utils.getJsonFromString(EntityUtils.toString(result.getEntity()));
            System.out.println(responseObj.get("success").asText());
            return responseObj.asText();
        }
        return "";
    }





}
