package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.service.ActivityFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * ActivityFilterResource controller
 */
@RestController
@RequestMapping("/api/activity-filter")
public class ActivityFilterResource {

  private final Logger log = LoggerFactory.getLogger(ActivityFilterResource.class);

  private final ActivityFilterService activityFilterService;

  public ActivityFilterResource(ActivityFilterService activityFilterService) {
    this.activityFilterService = activityFilterService;
  }

  /**
   * GET getPostalCodes
   */
  @GetMapping("/get-postal-codes")
  public Set<String> getPostalCodes() {
    return activityFilterService.getPostalCodesForUserSystemAccount();
  }

  /**
  * GET getCounties
  */
  @GetMapping("/get-regions")
  public Set<String> getRegions() {
  return activityFilterService.getRegionsForUserSystemAccount();
  }

  /**
  * GET getCities
  */
  @GetMapping("/get-cities")
  public Set<String> getCities() {
  return activityFilterService.getCitiesForUserSystemAccount();
  }
}
