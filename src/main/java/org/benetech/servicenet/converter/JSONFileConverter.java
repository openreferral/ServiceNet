package org.benetech.servicenet.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class JSONFileConverter extends AbstractFileConverter {

    @Override
    public ImportData convert(MultipartFile file) throws IOException {
        ImportData conversionOutput = new ImportData();
        conversionOutput.setJson(new String(file.getBytes()));
        return conversionOutput;
    }
}
