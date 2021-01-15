package org.benetech.servicenet.service;

import java.io.IOException;

public interface StringGZIPService {
    byte[] compress(String data)  throws IOException;

    String decompress(byte[] compressed) throws IOException;
}
