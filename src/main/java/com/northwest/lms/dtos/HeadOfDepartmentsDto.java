package com.northwest.lms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HeadOfDepartmentsDto {
    private long employeeId;
    private long departmentId;
}
