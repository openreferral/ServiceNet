package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {

    private String name;

    private String description;

    private Date nextFireDate;

    private Date prevFireDate;

    private String state;
}
