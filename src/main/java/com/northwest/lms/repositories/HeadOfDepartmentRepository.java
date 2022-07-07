package com.northwest.lms.repositories;

import com.northwest.lms.models.Department;
import com.northwest.lms.models.HeadOfDepartments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadOfDepartmentRepository extends JpaRepository<HeadOfDepartments, Long> {
    HeadOfDepartments findHeadOfDepartmentsByDepartment(Department department);
}
