package org.benetech.servicenet.converter;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileConverter extends AbstractFileConverter {

    private char delimiter = ',';

    public CSVFileConverter(String delimiter) {
        if (delimiter != null) {
            this.delimiter = delimiter.charAt(0);
        }
    }

    @Override
    public String convert(MultipartFile file) throws IOException {
        List<CSVRecord> originalRecords = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter).parse(
            new InputStreamReader(file.getInputStream())).getRecords();

        List<Object> convertedRecords = new ArrayList<>();
        Gson gson = new Gson();

        for (CSVRecord record : originalRecords) {
            convertedRecords.add(record.toMap());
        }

        return gson.toJson(convertedRecords);
    }
}
