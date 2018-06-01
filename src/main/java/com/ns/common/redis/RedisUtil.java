/**
 * project name: ns-crm
 * file name:RedisUtil
 * package name:com.ns.common.redis
 * date:2018-03-01 10:09
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.common.redis;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;

/**
 * description: //TODO <br>
 * date: 2018-03-01 10:09
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class RedisUtil {
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = Redis.use().getJedis();
            result = jedis.set(key, value);
        } finally {
            jedis.close();
        }
        return result;
    }
    public static String get(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = Redis.use().getJedis();
            result = jedis.set(key, value);
        } finally {
            jedis.close();
        }
        return result;
    }

}
