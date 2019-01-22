package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ICarolDataToPersist {

    private List<ICarolAgency> agencies = new ArrayList<>();

    private List<ICarolProgram> programs = new ArrayList<>();

    private List<ICarolServiceSite> serviceSites = new ArrayList<>();

    private List<ICarolSite> sites = new ArrayList<>();

    public void addAgency(ICarolAgency agency) {
        agencies.add(agency);
    }

    public void addProgram(ICarolProgram program) {
        programs.add(program);
    }

    public void addServiceSite(ICarolServiceSite serviceSite) {
        serviceSites.add(serviceSite);
    }

    public void addSite(ICarolSite site) {
        sites.add(site);
    }
}
