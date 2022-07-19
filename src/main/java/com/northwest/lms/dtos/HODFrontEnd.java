package com.northwest.lms.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HODFrontEnd {
    private long id;
    private String department;
    private long deptId;
    private String employee;
    private long empId;
}
