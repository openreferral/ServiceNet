package org.benetech.servicenet.service;

public interface MongoDbService {

    String saveDocument(String json);

    String findDocumentById(String id);
}
