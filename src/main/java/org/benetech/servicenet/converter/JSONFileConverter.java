package org.benetech.servicenet.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class JSONFileConverter extends AbstractFileConverter {

    @Override
    public String convert(MultipartFile file) throws IOException {
        return new String(file.getBytes());
    }
}
