package org.benetech.servicenet.service;

public interface MongoDbService {

    String saveParsedDocument(String data, String type, String delimiter);

    String saveOriginalDocument(byte[] bytes, String type, String delimiter);

    String findOriginalDocumentById(String id);

    String findParsedDocumentById(String id);
}
