package org.benetech.servicenet.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class StreamUtils {

    private StreamUtils() { }

    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    public static File stream2file(InputStream in) throws IOException {
        final File tempFile = temporaryFile();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
    public static File temporaryFile() throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static void writeJsonStream(File file, List<Object> objects) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            writeJsonStream(out, objects);
        }
    }

    public static void writeJsonStream(OutputStream out, List<Object> objects) throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginArray();
        for (Object obj : objects) {
            gson.toJson(obj, Object.class, writer);
        }
        writer.endArray();
        writer.close();
    }

}
