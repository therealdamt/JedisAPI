package xyz.damt.redis.packet;

public interface RedisPacket {

    /**
     * Redis Packet Interact | Class must implement
     *
     * @Method onReceived | Method to execute when a message is received
     * @Method onSend | Method to execute when a message is sent [Optional]
     */

    void onReceived();
    default void onSend() {}

}
