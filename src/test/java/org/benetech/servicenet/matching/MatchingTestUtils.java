package org.benetech.servicenet.matching;

import static org.benetech.servicenet.adapter.AdapterTestsUtils.readResourceAsSteam;

import java.io.IOException;
import java.io.InputStream;
import org.benetech.servicenet.converter.CSVFileConverter;

public class MatchingTestUtils {

    public static String loadCsv(String fileName) throws IOException {
        CSVFileConverter csvFileConverter = new CSVFileConverter(",");
        InputStream csvIs = readResourceAsSteam(fileName);
        String res = csvFileConverter.convert(csvIs);
        csvIs.close();
        return res;
    }
}
