package com.northwest.lms.models;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;
    @Column(unique = true)
    private String DepartmentName;
}
