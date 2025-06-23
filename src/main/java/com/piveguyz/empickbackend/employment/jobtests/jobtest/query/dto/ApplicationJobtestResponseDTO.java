package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationJobtestResponseDTO {
    private int id;
    private int applicationId;
    private int jobtestId;
    private String jobtestTitle;
}
