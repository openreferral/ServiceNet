package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;

import java.util.UUID;

@Data
public class FileInfo {

    private UUID id;
    private String dateUploaded;
    private String originalDocumentId;
    private String parsedDocumentId;
    private String uploaderId;
    private String uploaderLogin;
}
