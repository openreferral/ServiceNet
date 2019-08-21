package org.benetech.servicenet.service;

import java.util.Set;

public interface ActivityFilterService {

    /**
     * Get all the Postal Codes
     *
     * @return the list of Postal Codes
     */
    Set<String> getPostalCodes();

    /**
     * Get all the Regions
     *
     * @return the list of Regions
     */
    Set<String> getRegions();

    /**
     * Get all the Cities
     *
     * @return the list of Cities
     */
    Set<String> getCities();

    /**
     * Get all the Taxonomies
     *
     * @return the list of Taxonomies
     */
    Set<String> getTaxonomies();
}
