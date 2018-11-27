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

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private DocumentUploadService documentUploadService;

    @Override
    public String saveDocument(String json) {

        Document document = new Document();

        BasicDBObject documentDetail = new BasicDBObject();
        documentDetail.put(DATA, json);
        document.put(DETAIL, documentDetail);
        ObjectId id = new ObjectId();
        document.put(ID, id);

        getDocumentsCollection().insertOne(document);

        return id.toHexString();
    }

    @Override
    public String findDocumentById(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID, new ObjectId(id));

        Document document = (Document) mongoDbFactory.getDb()
            .getCollection(DOCUMENTS_COLLECTION).find(query).first().get(DETAIL);
        return document.get(DATA).toString();
    }

    private MongoCollection<Document> getDocumentsCollection() {
        return mongoDbFactory.getDb().getCollection(DOCUMENTS_COLLECTION);
    }
}
