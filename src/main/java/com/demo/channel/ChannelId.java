package com.demo.channel;

import java.io.Serializable;

/**
 * Created by jiangfei on 2019/10/26.
 */
public interface ChannelId extends Serializable, Comparable<ChannelId> {

    String asShortText();

    String asLongText();
}
