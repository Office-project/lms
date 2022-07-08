package com.northwest.lms.dtos;

import com.northwest.lms.enums.Gender;
import com.northwest.lms.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeData {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String  gender;
    private String department;
    private String location;
    private LocalDate joinDate;
    private String  role;
}
