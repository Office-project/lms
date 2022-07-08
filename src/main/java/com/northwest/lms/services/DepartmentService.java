package com.northwest.lms.services;

import com.northwest.lms.dtos.DepartmentDto;
import com.northwest.lms.dtos.HeadOfDepartmentsDto;
import com.northwest.lms.dtos.LeaveTypeDto;
import com.northwest.lms.models.Department;
import com.northwest.lms.models.HeadOfDepartments;
import com.northwest.lms.models.LeaveType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService {
    ResponseEntity<Department> createDepartment(DepartmentDto departmentDto);
    ResponseEntity<HeadOfDepartments> assignHOD(HeadOfDepartmentsDto headOfDepartmentsDto);

    ResponseEntity<List<HeadOfDepartments>> fetchAllHods();
    ResponseEntity<HeadOfDepartments> updateHOD(HeadOfDepartmentsDto headOfDepartmentsDto,Long hodId);

    ResponseEntity<LeaveType> createLeaveType (LeaveTypeDto leaveTypeDto);
    ResponseEntity<List<LeaveType>> fetchAllLeaveTypes();

    ResponseEntity<List<Department>> getAll();
}
