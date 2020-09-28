package com.google.client.util;

import java.io.IOException;

public interface BackOff {

    static final long STOP = -1L;

    void reset() throws IOException;

    long nextBackOffMillis() throws IOException;

    BackOff ZERO_BACKOFF = new BackOff() {

        public void reset() throws IOException {
        }

        public long nextBackOffMillis() throws IOException {
            return 0;
        }
    };

    BackOff STOP_BACKOFF = new BackOff() {

        public void reset() throws IOException {
        }

        public long nextBackOffMillis() throws IOException {
            return STOP;
        }
    };

}
