package org.benetech.servicenet.service.impl;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.io.IOException;
import org.benetech.servicenet.service.MongoDbService;
import org.benetech.servicenet.service.StringGZIPService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

@Service
public class MongoDbServiceImpl implements MongoDbService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private static final String DOCUMENTS_COLLECTION = "documents";
    private static final String DETAIL = "detail";
    private static final String ID = "_id";
    private static final String DATA = "data";
    private static final String PARSED = "parsed";

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private StringGZIPService stringGZIPService;

    @Override
    public String saveParsedDocument(String data)  {
        try {
            byte[] compressed = stringGZIPService.compress(data);
            return saveDocument(encodeBase64String(compressed), true);
        } catch (IOException e) {
            log.debug("Error while saving parsed document", e);
            return null;
        }
    }

    @Override
    public String saveOriginalDocument(byte[] bytes) {
        return saveDocument(bytes, false);
    }

    @Override
    public String findOriginalDocumentById(String id) {
        return findDocumentById(id).toString();
    }

    @Override
    public String findParsedDocumentById(String id) {
        String dataFromDb = findDocumentById(id).toString();
        try {
            return stringGZIPService.decompress(decodeBase64(dataFromDb));
        } catch (IOException e) {
            log.debug("Error while reading parsed document", e);
            return null;
        }
    }

    private Object findDocumentById(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID, new ObjectId(id));

        Document document = (Document) mongoDbFactory.getDb()
            .getCollection(DOCUMENTS_COLLECTION).find(query).first().get(DETAIL);
        return document.get(DATA);
    }

    private String saveDocument(Object file, boolean isParsed) {
        Document document = new Document();

        BasicDBObject documentDetail = new BasicDBObject();
        documentDetail.put(DATA, file);
        documentDetail.put(PARSED, isParsed);
        document.put(DETAIL, documentDetail);
        ObjectId id = new ObjectId();
        document.put(ID, id);

        getDocumentsCollection().insertOne(document);

        return id.toHexString();
    }

    private MongoCollection<Document> getDocumentsCollection() {
        return mongoDbFactory.getDb().getCollection(DOCUMENTS_COLLECTION);
    }
}
