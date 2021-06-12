package xyz.damt.redis;

import com.google.gson.Gson;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.damt.redis.listener.RedisListener;
import xyz.damt.redis.packet.RedisPacket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class RedisHandler {

    private final String host, password, channel;
    private final int port;
    private final boolean isAuth;

    private final Gson gson;
    private final Executor executor;
    private final RedisListener redisListener;

    private JedisPool jedisPool;
    private Jedis jedis;

    /**
     * Redis Handler Constructor
     *
     * @param host     | Host Of Redis Server
     * @param password | Password If Auth Is Enabled
     * @param channel  | Redis Server Channel To Send Messages
     * @param port     | Port Of Redis Server
     * @param isAuth   | If auth is enabled
     */

    public RedisHandler(String host, String password, String channel, int port, boolean isAuth) {
        this.host = host;
        this.password = password;
        this.channel = channel;

        this.gson = new Gson();

        this.port = port;
        this.isAuth = isAuth;
        this.executor = Executors.newFixedThreadPool(2);

        this.redisListener = new RedisListener(executor, this);
        this.connect();
    }

    /**
     * Connect Method
     */

    private void connect() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 2000);

        try (Jedis jedis = jedisPool.getResource()) {
            this.jedis = jedis;
            if (isAuth) jedis.auth(password);

            jedis.connect();
            jedis.subscribe(redisListener, channel);
        }
    }

    /**
     * Send Method To Send Packets
     *
     * @param redisPacket | Packet To Send
     */

    public void send(RedisPacket redisPacket) {
        redisPacket.onSend();

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                this.jedis = jedis;
                if (isAuth) jedis.auth(password);

                jedis.publish(channel, gson.toJson(redisPacket) + "///" + redisPacket.getClass().getName());
            }
        }).start();
    }

    /**
     * Jedis Cache Ping
     *
     * @return get Redis's Ping
     */

    public String getPing() {
        return jedis.ping();
    }

    /**
     * Returns if the Jedis is connected
     *
     * @return Redis Connection Status
     */

    public boolean isConnected() {
        return jedis.isConnected();
    }

    /**
     * Shutdown Method
     */

    public void shutdown() {
        jedisPool.destroy();
        redisListener.unsubscribe();
    }

}
