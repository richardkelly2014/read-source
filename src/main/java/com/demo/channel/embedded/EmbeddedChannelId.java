package com.demo.channel.embedded;

import com.demo.channel.ChannelId;

/**
 * Created by jiangfei on 2019/10/27.
 */
final class EmbeddedChannelId implements ChannelId {

    private static final long serialVersionUID = -251711922203466130L;

    static final ChannelId INSTANCE = new EmbeddedChannelId();

    private EmbeddedChannelId() {
    }

    @Override
    public String asShortText() {
        return toString();
    }

    @Override
    public String asLongText() {
        return toString();
    }

    @Override
    public int compareTo(final ChannelId o) {
        if (o instanceof EmbeddedChannelId) {
            return 0;
        }

        return asLongText().compareTo(o.asLongText());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmbeddedChannelId;
    }

    @Override
    public String toString() {
        return "embedded";
    }

}
