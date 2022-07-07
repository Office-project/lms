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
    private LocalDate start;
    private LocalDate end;
    private String reasonForRequest;
    private String reasonForDecline;
    private String reasonForCancellation;
    private String reliefOfficer;
    private LocalDate appliedOn;
    private String download;
}
