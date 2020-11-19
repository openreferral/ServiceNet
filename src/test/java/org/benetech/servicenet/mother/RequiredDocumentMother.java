package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.RequiredDocument;

public final class RequiredDocumentMother {

    public static final String REQUIRED_DOCUMENT = "document";

    public static RequiredDocument createDefault() {
        return new RequiredDocument()
            .document(REQUIRED_DOCUMENT);
    }

    private RequiredDocumentMother() {
    }
}
