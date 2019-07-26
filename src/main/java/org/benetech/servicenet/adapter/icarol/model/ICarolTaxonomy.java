package org.benetech.servicenet.adapter.icarol.model;

import java.util.List;
import lombok.Data;

@Data
public class ICarolTaxonomy {

    private String code;

    private String databaseID;

    private String name;

    private String path;

    private String definition;

    private String parentCode;

    private List<String> synonyms;
}
