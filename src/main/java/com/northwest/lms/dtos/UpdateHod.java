package com.northwest.lms.dtos;

import lombok.*;

@Builder
@Setter@Getter @NoArgsConstructor @AllArgsConstructor
public class UpdateHod {
    private long hodId;
    private long empId;
}
