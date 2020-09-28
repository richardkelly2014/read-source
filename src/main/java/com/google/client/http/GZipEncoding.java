package com.google.client.http;

import com.google.client.util.StreamingContent;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GZipEncoding implements HttpEncoding  {

    public String getName() {
        return "gzip";
    }

    public void encode(StreamingContent content, OutputStream out) throws IOException {

        OutputStream out2 = new BufferedOutputStream(out) {
            @Override
            public void close() throws IOException {
                // copy implementation of super.close(), except do not close the underlying output stream
                try {
                    flush();
                } catch (IOException ignored) {
                }
            }
        };

        GZIPOutputStream zipper = new GZIPOutputStream(out2);
        content.writeTo(zipper);
        // cannot call just zipper.finish() because that would cause a severe memory leak
        zipper.close();

    }
}
