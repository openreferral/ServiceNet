package org.benetech.servicenet.converter;

import static org.benetech.servicenet.util.StreamUtils.temporaryFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.benetech.servicenet.util.StreamUtils;
import org.xml.sax.SAXException;

public class XlsxFileConverter extends AbstractDataConverter {

    public XlsxFileConverter() {
    }

    public File convertToFile(InputStream is) throws IOException, OpenXML4JException, SAXException {
        File xlsx = StreamUtils.stream2file(is);
        is.close();
        return convert(xlsx);
    }

    public ImportData convert(InputStream is) throws IOException, OpenXML4JException, SAXException {
        File xlsx = StreamUtils.stream2file(is);
        is.close();
        File jsonFile = convert(xlsx);
        ImportData conversionOutput = new ImportData();
        conversionOutput.setTemporaryFile(jsonFile);
        return conversionOutput;
    }

    public File convert(File xlsx) throws IOException, OpenXML4JException, SAXException {
        String csv = XLSX2CSV.convert(xlsx, 0);
        CSVFileConverter csvFileConverter = new CSVFileConverter(",");
        return csvFileConverter.convertToFile(csv);
    }

    @Override
    public ImportData convert(String content) throws IOException {
        final File tempFile = temporaryFile();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            out.write(content.getBytes());
        }
        ImportData conversionOutput = new ImportData();
        conversionOutput.setTemporaryFile(tempFile);
        return conversionOutput;
    }
}
