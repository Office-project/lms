package com.northwest.lms.models;

import com.northwest.lms.enums.Gender;
import com.northwest.lms.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "department_dept_id")
    private Department department;
    private LocalDate joinDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany
    @ToString.Exclude
    private List<LeaveType> leavesApplicable;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
