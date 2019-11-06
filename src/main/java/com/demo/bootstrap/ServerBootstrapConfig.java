package com.demo.bootstrap;

import com.demo.channel.ChannelHandler;
import com.demo.channel.ChannelOption;
import com.demo.channel.EventLoopGroup;
import com.demo.channel.ServerChannel;
import com.demo.util.AttributeKey;
import com.demo.util.internal.StringUtil;

import java.util.Map;

/**
 * Created by jiangfei on 2019/11/6.
 */
public final class ServerBootstrapConfig extends AbstractBootstrapConfig<ServerBootstrap, ServerChannel> {

    ServerBootstrapConfig(ServerBootstrap bootstrap) {
        super(bootstrap);
    }

    /**
     * Returns the configured {@link EventLoopGroup} which will be used for the child channels or {@code null}
     * if non is configured yet.
     */
    @SuppressWarnings("deprecation")
    public EventLoopGroup childGroup() {
        return bootstrap.childGroup();
    }

    /**
     * Returns the configured {@link ChannelHandler} be used for the child channels or {@code null}
     * if non is configured yet.
     */
    public ChannelHandler childHandler() {
        return bootstrap.childHandler();
    }

    /**
     * Returns a copy of the configured options which will be used for the child channels.
     */
    public Map<ChannelOption<?>, Object> childOptions() {
        return bootstrap.childOptions();
    }

    /**
     * Returns a copy of the configured attributes which will be used for the child channels.
     */
    public Map<AttributeKey<?>, Object> childAttrs() {
        return bootstrap.childAttrs();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        buf.append(", ");
        EventLoopGroup childGroup = childGroup();
        if (childGroup != null) {
            buf.append("childGroup: ");
            buf.append(StringUtil.simpleClassName(childGroup));
            buf.append(", ");
        }
        Map<ChannelOption<?>, Object> childOptions = childOptions();
        if (!childOptions.isEmpty()) {
            buf.append("childOptions: ");
            buf.append(childOptions);
            buf.append(", ");
        }
        Map<AttributeKey<?>, Object> childAttrs = childAttrs();
        if (!childAttrs.isEmpty()) {
            buf.append("childAttrs: ");
            buf.append(childAttrs);
            buf.append(", ");
        }
        ChannelHandler childHandler = childHandler();
        if (childHandler != null) {
            buf.append("childHandler: ");
            buf.append(childHandler);
            buf.append(", ");
        }
        if (buf.charAt(buf.length() - 1) == '(') {
            buf.append(')');
        } else {
            buf.setCharAt(buf.length() - 2, ')');
            buf.setLength(buf.length() - 1);
        }

        return buf.toString();
    }

}
