package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EdenDataToPersist {

    private List<EdenAgency> agencies = new ArrayList<>();

    private List<EdenProgram> programs = new ArrayList<>();

    private List<EdenProgramAtSite> programAtSites = new ArrayList<>();

    private List<EdenSite> sites = new ArrayList<>();

    public void addAgency(EdenAgency agency) {
        agencies.add(agency);
    }

    public void addProgram(EdenProgram program) {
        programs.add(program);
    }

    public void addProgramAtSite(EdenProgramAtSite programAtSite) {
        programAtSites.add(programAtSite);
    }

    public void addSite(EdenSite site) {
        sites.add(site);
    }
}
