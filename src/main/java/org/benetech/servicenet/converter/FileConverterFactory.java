package org.benetech.servicenet.converter;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public final class FileConverterFactory {

    public static AbstractFileConverter getConverter(MultipartFile file, String delimiter) throws IllegalArgumentException {
        String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        FileExtension extension;
        if (extensionName != null) {
            extension = FileExtension.valueOf(extensionName.toUpperCase(Locale.ROOT));
        } else {
            throw new IllegalArgumentException("File extension wasn't be found");
        }
        switch (extension) {
            case CSV:
                return new CSVFileConverter(delimiter);
            case JSON:
                return new JSONFileConverter();
            case XLSX:
                return new XlsxFileConverter();
            default:
                throw new IllegalArgumentException("File type not supported");
        }
    }

    private FileConverterFactory() {
    }
}
