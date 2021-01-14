package org.benetech.servicenet.converter;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.benetech.servicenet.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileConverter extends AbstractFileConverter {
    private final Logger log = LoggerFactory.getLogger(CSVFileConverter.class);

    private char delimiter = ',';

    public CSVFileConverter(String delimiter) {
        if (delimiter != null) {
            this.delimiter = delimiter.charAt(0);
        }
    }

    private File convertRecords(List<CSVRecord> originalRecords) {
        List<Object> convertedRecords = new ArrayList<>();

        for (CSVRecord record : originalRecords) {
            convertedRecords.add(record.toMap());
        }
        try {
            File tempFile = StreamUtils.temporaryFile();
            StreamUtils.writeJsonStream(tempFile, convertedRecords);
            return tempFile;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String convert(InputStream is) throws IOException {
        List<CSVRecord> originalRecords = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter).parse(
            new InputStreamReader(is)).getRecords();
        is.close();

        File file = convertRecords(originalRecords);
        return Files.readString(Paths.get(file.getPath()));
    }

    public File convertToFile(String csv) throws IOException {
        List<CSVRecord> originalRecords = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter).parse(
            new StringReader(csv)).getRecords();

        return convertRecords(originalRecords);
    }

    public String convert(String csv) throws IOException {
        File file = convertToFile(csv);
        return Files.readString(Paths.get(file.getPath()));
    }

    @Override
    public String convert(MultipartFile file) throws IOException {
        return convert(file.getInputStream());
    }
}
