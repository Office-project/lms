package com.northwest.lms.dtos;

import com.northwest.lms.enums.Gender;
import com.northwest.lms.enums.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Long departmentID;
    private Long locationId;
    private Long personalSupervisorId;
    private LocalDate joinDate;
    private Role role;
}
