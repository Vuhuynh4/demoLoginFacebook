/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mservice.demologinfacebook.cache;

/**
 *
 * @author vu.huynh
 */
@FunctionalInterface
public interface CacheTimeoutHandler {

    void handle(String requestId, Object data);

}
