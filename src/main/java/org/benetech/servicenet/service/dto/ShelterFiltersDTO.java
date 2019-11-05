package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ShelterFiltersDTO implements Serializable {

  private List<String> definedCoverageAreas = new ArrayList<>();

  private List<String> tags = new ArrayList<>();

  private String searchQuery;

  private UUID userId;

  private boolean showOnlyAvailableBeds;
}
