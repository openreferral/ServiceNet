package org.benetech.servicenet.converter;

import java.io.IOException;
import org.benetech.servicenet.util.BsonUtils;

public class JSONFileConverter extends AbstractDataConverter {

    @Override
    public ImportData convert(Object data) throws IOException {
        ImportData conversionOutput = new ImportData();
        conversionOutput.setJson(BsonUtils.docToString(data));
        return conversionOutput;
    }
}
