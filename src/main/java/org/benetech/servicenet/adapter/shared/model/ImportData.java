package org.benetech.servicenet.adapter.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.benetech.servicenet.domain.DocumentUpload;

@Data
@AllArgsConstructor
public class ImportData {

    private DocumentUpload documentUpload;

    private String providerName;
}
