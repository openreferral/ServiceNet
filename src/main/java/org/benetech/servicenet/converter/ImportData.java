package org.benetech.servicenet.converter;

import java.io.File;
import lombok.Data;

@Data
public class ImportData {
    private String json;
    private File temporaryFile;
}
