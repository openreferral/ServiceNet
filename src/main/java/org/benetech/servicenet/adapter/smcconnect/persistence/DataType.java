package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.smcconnect.model.SmcAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;
import org.benetech.servicenet.adapter.smcconnect.model.SmcContact;
import org.benetech.servicenet.adapter.smcconnect.model.SmcHolidaySchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcMailAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcPhone;
import org.benetech.servicenet.adapter.smcconnect.model.SmcProgram;
import org.benetech.servicenet.adapter.smcconnect.model.SmcRegularSchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;

enum DataType {
    LOCATIONS(SmcLocation.class),
    MAIL_ADDRESSES(SmcMailAddress.class),
    ORGANIZATIONS(SmcOrganization.class),
    PHONES(SmcPhone.class),
    ADDRESSES(SmcAddress.class),
    REGULAR_SCHEDULES(SmcRegularSchedule.class),
    HOLIDAY_SCHEDULES(SmcHolidaySchedule.class),
    SERVICES(SmcService.class),
    CONTACTS(SmcContact.class),
    PROGRAMS(SmcProgram.class);

    private Class<? extends SmcBaseData> clazz;

    DataType(Class<? extends SmcBaseData> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends SmcBaseData> getClazz() {
        return clazz;
    }
}
