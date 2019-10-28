package com.demo.channel;

/**
 * 最大消息 接受 ByteBuf 分配
 */
public interface MaxMessagesRecvByteBufAllocator extends RecvByteBufAllocator {

    int maxMessagesPerRead();

    MaxMessagesRecvByteBufAllocator maxMessagesPerRead(int maxMessagesPerRead);
}
