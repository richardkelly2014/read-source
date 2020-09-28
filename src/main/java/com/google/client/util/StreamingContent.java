package com.google.client.util;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingContent {

    void writeTo(OutputStream out) throws IOException;
}
