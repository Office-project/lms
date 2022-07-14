package com.northwest.lms.repositories;

import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmail(String email);
    List<Employee> findEmployeesByDepartment(Department department);
}
