package com.northwest.lms.models;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadOfDepartments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hodId;
    @OneToOne
    @JoinColumn(name = "department_dept_id")
    private Department department;
    @OneToOne
    @JoinColumn(name = "employee_emp_id")
    private Employee employee;
}
