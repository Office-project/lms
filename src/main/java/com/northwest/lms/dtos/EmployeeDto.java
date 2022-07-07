package com.northwest.lms.dtos;

import com.northwest.lms.enums.Gender;
import com.northwest.lms.enums.Role;
import com.northwest.lms.models.Department;
import com.northwest.lms.models.LeaveType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    private LocalDate joinDate;
    private Role role;
}
