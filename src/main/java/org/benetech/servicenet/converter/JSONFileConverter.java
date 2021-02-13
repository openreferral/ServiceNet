package org.benetech.servicenet.converter;

import java.io.IOException;

public class JSONFileConverter extends AbstractDataConverter {

    @Override
    public ImportData convert(String data) throws IOException {
        ImportData conversionOutput = new ImportData();
        conversionOutput.setJson(data);
        return conversionOutput;
    }
}
