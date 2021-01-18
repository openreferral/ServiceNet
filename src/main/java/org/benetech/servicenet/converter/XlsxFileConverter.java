package org.benetech.servicenet.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.benetech.servicenet.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

public class XlsxFileConverter extends AbstractFileConverter {
    private final Logger log = LoggerFactory.getLogger(XlsxFileConverter.class);

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
        conversionOutput.setJson(Files.readString(Paths.get(jsonFile.getPath())));
        return conversionOutput;
    }

    public File convert(File xlsx) throws IOException, OpenXML4JException, SAXException {
        String csv = XLSX2CSV.convert(xlsx, 0);
        CSVFileConverter csvFileConverter = new CSVFileConverter(",");
        return csvFileConverter.convertToFile(csv);
    }

    @Override
    public ImportData convert(MultipartFile file) throws IOException {
        try {
            return convert(file.getInputStream());
        } catch (OpenXML4JException | SAXException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
