package com.northwest.lms.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeaveTypeDto {
    private String leaveName;
    private int daysAllowed;
}
