package org.benetech.servicenet.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.MongoDbService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

@Service
public class MongoDbServiceImpl implements MongoDbService {

    private static final String DOCUMENTS_COLLECTION = "documents";
    private static final String DETAIL = "detail";
    private static final String ID = "_id";
    private static final String DATA = "data";
    private static final String PARSED = "parsed";

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private DocumentUploadService documentUploadService;

    @Override
    public String saveParsedDocument(String file) {
        return saveDocument(file, true);
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
        return findDocumentById(id).toString();
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
