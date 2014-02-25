package org.jldservice.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shicq on 2/25/14.
 */

class Cache {
    public static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

    static def get(key) {
        return cache.get(key);
    }

    static def put(key, val) {
        cache.put(key,val);
    }
}