package org.benetech.servicenet.service.impl;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.service.StringGZIPService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class StringGZIPServiceImplTest {

    @Autowired
    private StringGZIPService stringGZIPService;

    @Autowired
    private MongoDbServiceImpl mongoDbService;

    private static final String TEST_STRING = "AAAAAABBBBCCCCCC[]{},./;')(*&^%$#@!`~-_=+|\\;:'\",<.>/?";

    @Test
    public void shouldCompressAndDecompress() throws IOException {
        byte [] compressed = stringGZIPService.compress(TEST_STRING);
        String decompressed = stringGZIPService.decompress(compressed);
        assertEquals(TEST_STRING, decompressed);

    }

    @Test
    public void shouldCompressAndDecompressUsingMongo() throws IOException {
        byte [] compressed = stringGZIPService.compress(TEST_STRING);
        String id = mongoDbService.saveParsedDocument(encodeBase64String(compressed));
        String decompressed = mongoDbService.findParsedDocumentById(id);
        assertEquals(TEST_STRING, stringGZIPService.decompress(decodeBase64(decompressed)));
    }
}
