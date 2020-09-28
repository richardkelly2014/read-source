package com.google.client.http;

import com.google.client.util.StreamingContent;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpEncoding {

    String getName();


    void encode(StreamingContent content, OutputStream out) throws IOException;
}
