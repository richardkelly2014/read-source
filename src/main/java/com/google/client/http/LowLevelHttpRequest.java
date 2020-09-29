package com.google.client.http;

import com.google.client.util.StreamingContent;

import java.io.IOException;

public abstract class LowLevelHttpRequest {

    private long contentLength = -1;

    private String contentEncoding;

    private String contentType;

    private StreamingContent streamingContent;

    public abstract void addHeader(String name, String value) throws IOException;

    public final void setContentLength(long contentLength) throws IOException {
        this.contentLength = contentLength;
    }

    public final long getContentLength() {
        return contentLength;
    }

    public final void setContentEncoding(String contentEncoding) throws IOException {
        this.contentEncoding = contentEncoding;
    }

    public final String getContentEncoding() {
        return contentEncoding;
    }

    public final void setContentType(String contentType) throws IOException {
        this.contentType = contentType;
    }

    public final String getContentType() {
        return contentType;
    }

    /**
     * Sets the streaming content or {@code null} for no content.
     *
     * @throws IOException I/O exception
     * @since 1.14
     */
    public final void setStreamingContent(StreamingContent streamingContent)
            throws IOException {
        this.streamingContent = streamingContent;
    }

    /**
     * Returns the streaming content or {@code null} for no content.
     *
     * @since 1.14
     */
    public final StreamingContent getStreamingContent() {
        return streamingContent;
    }

    /**
     * Sets the connection and read timeouts.
     *
     * <p>
     * Default implementation does nothing, but subclasses should normally override.
     * </p>
     *
     * @param connectTimeout timeout in milliseconds to establish a connection or {@code 0} for an
     *        infinite timeout
     * @param readTimeout Timeout in milliseconds to read data from an established connection or
     *        {@code 0} for an infinite timeout
     * @throws IOException I/O exception
     * @since 1.4
     */
    public void setTimeout(int connectTimeout, int readTimeout) throws IOException {
    }

    /**
     * Sets the write timeout for POST/PUT requests.
     *
     * <p>
     * Default implementation does nothing, but subclasses should normally override.
     * </p>
     *
     * @param writeTimeout timeout in milliseconds to establish a connection or {@code 0} for an
     *        infinite timeout
     * @throws IOException I/O exception
     * @since 1.27
     */
    public void setWriteTimeout(int writeTimeout) throws IOException {
    }

    public abstract LowLevelHttpResponse execute() throws IOException;
}
