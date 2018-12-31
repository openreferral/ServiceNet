package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

import java.util.List;

@Data
public class DataToPersist {

    private List<Agency> agencies;

    private List<Program> programs;

    private List<ProgramAtSite> programAtSites;

    private List<Site> sites;
}
