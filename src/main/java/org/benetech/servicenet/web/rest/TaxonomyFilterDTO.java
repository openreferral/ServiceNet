package org.benetech.servicenet.web.rest;

import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxonomyFilterDTO {
    private String currentProvider;

    private Map<String, Set<String>> taxonomiesByProvider;
}
