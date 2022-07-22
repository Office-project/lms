package com.northwest.lms.dtos;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String type;
    private int duration;
    private String start;
    private String end;
    private String reasonForRequest;
    private String reasonForDecline;
    private String reasonForCancellation;
    private String reliefOfficer;
    private String appliedOn;
    private String download;

    private String name;
    private String relief;
    private Boolean reliefApproval;
    private String supervisor;
    private Boolean supervisorApproval;
    private String hod;
    private String adminName;
    private Boolean hodApproval;
    private Boolean adminApproval;
    private Boolean decision;
    private String status;
}
