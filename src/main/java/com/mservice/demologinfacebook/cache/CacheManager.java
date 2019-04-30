/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook.cache;

import com.mservice.demologinfacebook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author vu.huynh
 */
public class CacheManager {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    private static CacheManager INSTANCE = new CacheManager();

    private final Map<String, CacheData> dataMap = new ConcurrentHashMap<>();
    private final Object LOCK = new Object();

    private CacheManager() {
        doPerodic();
    }

    public static void init() {
        INSTANCE = new CacheManager();
    }

    public static CacheManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheManager();
        }
        return INSTANCE;
    }

    public void addCache(String key, String requestId, int timeout, Object data) {
        dataMap.put(key, new CacheData(requestId, timeout, data));
    }
    
    public Object getCache(String key) {
        CacheData cache = dataMap.get(key);
        if(cache == null) {
            return null;
        }
        return cache.getObject();
    }
    
    public Object getCaches(String key) {
        CacheData cache = dataMap.get(key);
        if(cache == null) {
            return null;
        }
        return cache.getObject();
    }

    private void doPerodic() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Utils.safeSleep(1000);
                    clearCache();
                }
            }

        }.start();
    }

    private void clearCache() {
        Set<String> keys = dataMap.keySet();
        keys.stream().forEach((key) -> {
            CacheData cacheData = dataMap.get(key);
            if (cacheData == null) {
                dataMap.remove(key);
            }
            else if (cacheData.isExpired()) {
                synchronized (LOCK) {
                    LOGGER.info("[{}][{}]--CACHE TIMEOUT. Clear wrapper key: [{}]", cacheData.getRequestId(), key.toLowerCase(), key);
                    dataMap.remove(key);
                }
            }

        });

    }
}
