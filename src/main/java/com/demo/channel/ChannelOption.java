package com.demo.channel;

import com.demo.buffer.ByteBufAllocator;
import com.demo.util.AbstractConstant;
import com.demo.util.ConstantPool;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 通道 配置 选项
 *
 * Created by jiangfei on 2019/10/27.
 */
public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>> {

    private static final ConstantPool<ChannelOption<Object>> pool =
            new ConstantPool<ChannelOption<Object>>() {
                @Override
                protected ChannelOption<Object> newConstant(int id, String name) {
                    return new ChannelOption<Object>(id, name);
                }
            };

    public static <T> ChannelOption<T> valueOf(String name) {
        return (ChannelOption<T>) pool.valueOf(name);
    }

    public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (ChannelOption<T>) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    public static <T> ChannelOption<T> newInstance(String name) {
        return (ChannelOption<T>) pool.newInstance(name);
    }

    //bytebuf 分配器
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
    //接收 byteBuf 分配器
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
    //消息大小处理
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");


    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");

    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");

    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");

    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK =
            valueOf("WRITE_BUFFER_WATER_MARK");

    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
    public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");

    /**
     * If {@code true} then the {@link Channel} is closed automatically and immediately on write failure.
     * The default value is {@code true}.
     */
    public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");

    public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
    public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
    public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
    public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
    public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
    public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
    public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
    public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");

    public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
    public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");

    public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");

    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION =
            valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");

    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP =
            valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");

    /**
     * Creates a new {@link ChannelOption} with the specified unique {@code name}.
     */
    private ChannelOption(int id, String name) {

        super(id, name);
    }

    @Deprecated
    protected ChannelOption(String name) {
        this(pool.nextId(), name);
    }

    /**
     * Validate the value which is set for the {@link ChannelOption}. Sub-classes
     * may override this for special checks.
     */
    public void validate(T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
    }
}
