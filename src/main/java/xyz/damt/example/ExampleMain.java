package xyz.damt.example;

import xyz.damt.example.packet.TestPacket;
import xyz.damt.redis.RedisHandler;

public final class ExampleMain {

    public static void main(String[] args) {
        RedisHandler redisHandler = new RedisHandler("127.0.0.1", "", "redis"
        , 6379, false);

        redisHandler.send(new TestPacket());
    }

}
