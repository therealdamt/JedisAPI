package xyz.damt.example.packet;

import xyz.damt.redis.packet.RedisPacket;

public class TestPacket implements RedisPacket {

    @Override
    public void onReceived() {
        System.out.println("cool bro!");
    }

    @Override
    public void onSend() {
        System.out.println("sent");
    }
}
