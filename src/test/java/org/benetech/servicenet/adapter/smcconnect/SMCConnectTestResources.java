package org.benetech.servicenet.adapter.smcconnect;

import java.util.Arrays;
import java.util.List;

final class SMCConnectTestResources {

    static final String SMCCONNECT = "smcconnect/";
    static final String MINIMAL = "minimal/";
    static final String COMPLETE = "complete/";
    static final String JSON = ".json";
    static final String PROVIDER = "SMCConnect";

    static final String ADDRESSES = "addresses";
    static final String CONTACTS = "contacts";
    static final String HOLIDAY_SCHEDULE = "holiday_schedules";
    static final String LOCATIONS = "locations";
    static final String MAIL_ADDRESSES = "mail_addresses";
    static final String ORGANIZATIONS = "organizations";
    static final String PHONES = "phones";
    static final String PROGRAMS = "programs";
    static final String REGULAR_SCHEDULES = "regular_schedules";
    static final String SERVICES = "services";

    static final List<Integer> DAYS = Arrays.asList(0, 1, 2, 3, 4);

    static final List<String> OPENS_AT = Arrays.asList("08:30", "09:00", "10:00",
        "13:00", "21:00");
    static final List<String> CLOSES_AT = Arrays.asList("03:00", "14:00", "16:30", "17:00");

}
