package com.google.client.http;

import com.google.client.util.StreamingContent;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpContent extends StreamingContent {
    /**
     * Returns the content length or less than zero if not known.
     */
    long getLength() throws IOException;

    /**
     * Returns the content type or {@code null} for none.
     */
    String getType();

    /**
     * Returns whether or not retry is supported on this content type.
     *
     * @since 1.4
     */
    boolean retrySupported();

    void writeTo(OutputStream out) throws IOException;
}
