package org.benetech.servicenet.service;

public interface MongoDbService {

    String saveParsedDocument(String file);

    String saveOriginalDocument(byte[] bytes);

    String findOriginalDocumentById(String id);

    byte[] findParsedDocumentById(String id);
}
