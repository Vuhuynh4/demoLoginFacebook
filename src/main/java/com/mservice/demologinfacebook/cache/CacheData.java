/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vu.huynh
 */
public class CacheData {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheData.class);
    
    private final Object object;
    private final String requestId;
    private final long createTime;
    private final CacheTimeoutHandler handler;

    private int expiredTime;

    public CacheData(String requestId, Integer expiredTime, CacheTimeoutHandler handler, Object object) {
        createTime = System.currentTimeMillis();
        this.object = object;
        this.requestId = requestId;
        this.expiredTime = expiredTime;
        this.handler = handler;
    }

    public CacheData(String requestId, int expiredTime, Object object) {
        this(requestId, expiredTime, null, object);
    }

    public long getLifeTime() {
        return System.currentTimeMillis() - createTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getObject() {
        return object;
    }
    
    public boolean isExpired() {
        return expiredTime > 0 ? (getLifeTime() > expiredTime) : false;

    }

    public void invokeTimeout() {
        handler.handle(requestId, object);
    }

    /**
     *
     * @param expiredTime: life time of this object. (milliseconds)
     */
    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

}
