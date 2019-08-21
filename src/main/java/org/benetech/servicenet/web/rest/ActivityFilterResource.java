package org.benetech.servicenet.web.rest;

import java.util.Set;
import org.benetech.servicenet.service.ActivityFilterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ActivityFilterResource controller
 */
@RestController
@RequestMapping("/api/activity-filter")
public class ActivityFilterResource {

    private final ActivityFilterService activityFilterService;

    public ActivityFilterResource(ActivityFilterService activityFilterService) {
        this.activityFilterService = activityFilterService;
    }

    /**
     * GET getPostalCodes
     */
    @GetMapping("/get-postal-codes")
    public Set<String> getPostalCodes() {
        return activityFilterService.getPostalCodes();
    }

    /**
     * GET getCounties
     */
    @GetMapping("/get-regions")
    public Set<String> getRegions() {
        return activityFilterService.getRegions();
    }

    /**
     * GET getCities
     */
    @GetMapping("/get-cities")
    public Set<String> getCities() {
        return activityFilterService.getCities();
    }

    /**
     * GET getTaxonomies
     */
    @GetMapping("/get-taxonomies")
    public Set<String> getTaxonomies() {
        return activityFilterService.getTaxonomies();
    }
}
