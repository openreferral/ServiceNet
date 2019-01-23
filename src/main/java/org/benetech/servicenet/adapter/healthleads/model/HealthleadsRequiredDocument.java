package org.benetech.servicenet.adapter.healthleads.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsRequiredDocument extends HealthleadsBaseData {

    private String document;
}
