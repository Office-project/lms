package com.northwest.lms.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {
    private long id;
    private String document;
    private String reason;
    private String position;
    private LocalDate startDate;
    private LocalDate resumptionDate;
    private String name;
    private String relief;
    private Boolean reliefApproval;
    private String supervisor;
    private Boolean supervisorApproval;
    private String hod;
    private Boolean hodApproval;
    private Boolean adminApproval;
    private Boolean decision;

}
