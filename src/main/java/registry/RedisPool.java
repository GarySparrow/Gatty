package registry;

import common.GattyConstant;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Logger;

public final class RedisPool {

    private static String ADDR = GattyConstant.REDISIP;

    private static int PORT = GattyConstant.REDIS_PORT;

    private static String AUTH = "gatty";

    private static int MAX_TOT = 200;

    private static int MAX_IDLE = 200;

    private static int MAX_WAIT = 10000;

    private static int TIMEOUT = 10000;

    private static redis.clients.jedis.JedisPool jedisPool = null;

    private static Logger logger = Logger.getLogger("server test");

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxTotal(MAX_TOT);
            config.setMaxIdle(MAX_IDLE);
            jedisPool = new redis.clients.jedis.JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}