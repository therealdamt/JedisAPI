package xyz.damt.redis.listener;

import redis.clients.jedis.JedisPubSub;
import xyz.damt.redis.RedisHandler;
import xyz.damt.redis.packet.RedisPacket;

import java.util.concurrent.Executor;

public class RedisListener extends JedisPubSub {

    private final Executor executor;
    private final RedisHandler redisHandler;

    /**
     * RedisListener Constructor
     * @param executor | Async Executor
     * @param redisHandler | RedisHandler
     */

    public RedisListener(Executor executor, RedisHandler redisHandler) {
        this.executor = executor;
        this.redisHandler = redisHandler;
    }

    /**
     * Event triggers when a Redis Message is received
     * Translates Packet Back From JSON
     * @param channel Channel being sent from
     * @param message Message received
     */

    @Override
    public void onMessage(String channel, String message) {
        if (redisHandler.isAuth()) redisHandler.getJedisPool().getResource().auth(redisHandler.getPassword());
        if (!channel.equalsIgnoreCase(redisHandler.getChannel())) return;

        executor.execute(() -> {
            String[] strings = message.split("||");

            System.out.println(strings[1]);
            System.out.println(strings[0]);

            Object jsonObject = null;
            try {
                jsonObject = redisHandler.getGson().fromJson(strings[0], Class.forName(strings[1]));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            RedisPacket redisPacket = (RedisPacket) jsonObject;

            if (redisPacket == null) {
                System.out.println("The redis packet received seems to be null!");
                return;
            }

            redisPacket.onReceived();
        });
    }
}
