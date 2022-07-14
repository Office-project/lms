package com.northwest.lms.services;

import com.northwest.lms.dtos.*;
import com.northwest.lms.models.Employee;

import com.northwest.lms.models.Location;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    ResponseEntity<Employee> createEmployee(EmployeeDto employeeDto);
    ResponseEntity<Employee> updateEmployee(Long Id, EmployeeDto employeeDto);
    ResponseEntity<List<Employee>> fetchAllEmployee();
    ResponseEntity<Employee> changePassword(ChangePasswordDto passwordDto);

    ResponseEntity<Location> createLocation(LocationDto loactionDto);

    ResponseEntity<List<UserOption>> getUserOption();

    ResponseEntity<List<LocationOption>> getLocationOption();

    ResponseEntity<List<DepartmentOption>> getDepartmentOption();

    ResponseEntity<List<LeaveTypeOption>> getLeaveTypeOption();
    ResponseEntity<List<Location>> findAllLocation();
    ResponseEntity<List<EmployeeData>> getEmpData();

    ResponseEntity<List<UserOption>> getUserOptions();
}
