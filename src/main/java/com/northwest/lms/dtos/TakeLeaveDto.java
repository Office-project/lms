package com.northwest.lms.dtos;

import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import com.northwest.lms.models.LeaveType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TakeLeaveDto {
    private Long reliefOfficerId;
    private Long leaveTypeId;
    private LocalDate startDate;
}
