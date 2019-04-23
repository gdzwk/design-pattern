package com.zwk.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 池测试
 * @author zwk
 */
public class PoolTest {

    public static void main(String[] args) throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(2);
        config.setBlockWhenExhausted(false);

        String host = "132.110.64.240";
        int port = 18850;
        JedisPool pool = new JedisPool(config, host, port);
        System.out.println(pool.getNumActive());
        Jedis jedis1 = pool.getResource();
        Jedis jedis2 = pool.getResource();
        Jedis jedis3 = pool.getResource();
        Jedis jedis4 = pool.getResource();
    }

}

// database=0
// host=132.110.64.240
// port=18850