package com.northwest.lms.controllers;


import com.northwest.lms.dtos.*;
import com.northwest.lms.models.*;
import com.northwest.lms.repositories.DepartmentRepository;
import com.northwest.lms.services.DepartmentService;
import com.northwest.lms.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private EmployeeService employeeService;
    private DepartmentService departmentService;

    @PostMapping("/staff")
    public ResponseEntity<Employee> createStaff(@RequestBody EmployeeDto employeeDto){
        return employeeService.createEmployee(employeeDto);
    }
    @GetMapping("/staff")
    public ResponseEntity<List<Employee>> fetchAllEmployee(){
        return employeeService.fetchAllEmployee();
    }

    @PostMapping("/department")
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDto deptDto){
        return departmentService.createDepartment(deptDto);
    }

    @GetMapping("/department")
    public ResponseEntity<List<Department>> fetchAllDepartment(){
        return departmentService.getAll();
    }

    @PostMapping("leave_types")
    public ResponseEntity<LeaveType> createLeaveType(@RequestBody LeaveTypeDto leaveTypeDto){
        return departmentService.createLeaveType(leaveTypeDto);
    }
    @GetMapping("leave_types")
    public ResponseEntity<List<LeaveType>> fetchAllLeaveTypes(){
        return departmentService.fetchAllLeaveTypes();
    }

    @PostMapping("/hod")
    public ResponseEntity<HeadOfDepartments> assignHod(@RequestBody HeadOfDepartmentsDto hodDto){
        return departmentService.assignHOD(hodDto);
    }

    @PostMapping("/hod/update")
    public ResponseEntity<HeadOfDepartments> updateHOD(@RequestBody UpdateHod updateHod){
        return departmentService.updateHod(updateHod);
    }


    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@RequestBody LocationDto loactionDto){
        return employeeService.createLocation(loactionDto);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> fetchAllLocation(){
        return employeeService.findAllLocation();
    }
}
