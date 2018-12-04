package org.benetech.servicenet.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class AbstractFileConverter {

    /**
     * @param file file to be converted to unified format (JSON)
     * @return converted file in JSON format
     * @throws IOException if there's problem with reading the file
     */
    public abstract String convert(MultipartFile file) throws IOException;
}
