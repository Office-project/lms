package com.northwest.lms.controllers;

import com.northwest.lms.dtos.*;
import com.northwest.lms.services.EmployeeService;
import com.northwest.lms.services.TakeLeaveService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class FrontEndController {
    private EmployeeService employeeService;
    private TakeLeaveService leaveService;

    @GetMapping("/user_option")
    public ResponseEntity<List<UserOption>> getUserOption(){
        return employeeService.getUserOption();
    }
    @GetMapping("/user_options_dept")
    public ResponseEntity<List<UserOption>> getUserOptions(){
        return employeeService.getUserOptions();
    }

    @GetMapping("/location_option")
    public ResponseEntity<List<LocationOption>> getLocationOption(){
        return employeeService.getLocationOption();
    }
    @GetMapping("/leave_option")
    public ResponseEntity<List<LeaveTypeOption>> getLeaveTypeOption(){
        return employeeService.getLeaveTypeOption();
    }
    @GetMapping("/department_option")
    public ResponseEntity<List<DepartmentOption>> getDepartmentOption(){
        return employeeService.getDepartmentOption();
    }

    @GetMapping ("/emp-data")
    public ResponseEntity<List<EmployeeData>> getEmployeeData(){
        return employeeService.getEmpData();
    }
}
