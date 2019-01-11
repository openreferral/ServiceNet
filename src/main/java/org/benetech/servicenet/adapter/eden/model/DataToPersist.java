package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataToPersist {

    private List<Agency> agencies = new ArrayList<>();

    private List<Program> programs = new ArrayList<>();

    private List<ProgramAtSite> programAtSites = new ArrayList<>();

    private List<Site> sites = new ArrayList<>();

    public void addAgency(Agency agency) {
        agencies.add(agency);
    }

    public void addProgram(Program program) {
        programs.add(program);
    }

    public void addProgramAtSite(ProgramAtSite programAtSite) {
        programAtSites.add(programAtSite);
    }

    public void addSite(Site site) {
        sites.add(site);
    }
}
