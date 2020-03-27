package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.UserProfile;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DocumentUploadMother {

    private static final ZonedDateTime DEFAULT_DATE_UPLOADED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime DIFFERENT_DATE_UPLOADED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ORIGINAL_DOCUMENT_ID = "DEFAULT_ORIGINAL_DOCUMENT_ID";
    private static final String DIFFERENT_ORIGINAL_DOCUMENT_ID = "DIFFERENT_ORIGINAL_DOCUMENT_ID";

    private static final String DEFAULT_PARSED_DOCUMENT_ID = "DEFAULT_PARSED_DOCUMENT_ID";
    private static final String DIFFERENT_PARSED_DOCUMENT_ID = "DIFFERENT_PARSED_DOCUMENT_ID";

    public static DocumentUpload createDefault() {
        return new DocumentUpload()
            .dateUploaded(DEFAULT_DATE_UPLOADED)
            .originalDocumentId(DEFAULT_ORIGINAL_DOCUMENT_ID)
            .parsedDocumentId(DEFAULT_PARSED_DOCUMENT_ID)
            .uploader(UserMother.createDefault());
    }

    public static DocumentUpload createDifferent() {
        return new DocumentUpload()
            .dateUploaded(DIFFERENT_DATE_UPLOADED)
            .originalDocumentId(DIFFERENT_ORIGINAL_DOCUMENT_ID)
            .parsedDocumentId(DIFFERENT_PARSED_DOCUMENT_ID)
            .uploader(UserMother.createDifferent());
    }

    public static DocumentUpload createDefaultAndPersist(EntityManager em) {
        UserProfile userProfile = UserMother.createDefaultAndPersist(em);
        DocumentUpload documentUpload = createDefault();
        documentUpload.setUploader(userProfile);
        em.persist(documentUpload);
        em.flush();
        return documentUpload;
    }

    public static DocumentUpload createDifferentAndPersist(EntityManager em) {
        UserProfile userProfile = UserMother.createDifferentAndPersist(em);
        DocumentUpload documentUpload = createDifferent();
        documentUpload.setUploader(userProfile);
        em.persist(documentUpload);
        em.flush();
        return documentUpload;
    }

    private DocumentUploadMother() {
    }
}
