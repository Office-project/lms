package com.northwest.lms.controllers;

import com.northwest.lms.dtos.*;
import com.northwest.lms.models.HeadOfDepartments;
import com.northwest.lms.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class FrontEndController {
    private EmployeeService employeeService;
    @GetMapping("/user_option")
    public ResponseEntity<List<UserOption>> getUserOption(){
        return employeeService.getUserOption();
    }
    @GetMapping("/user_options_dept")
    public ResponseEntity<List<UserOption>> getUserOptions(){
        return employeeService.getUserOptions();
    }
    @GetMapping("/user_options_dept/{id}")
    public ResponseEntity<List<UserOption>> getUserOptionsByDept(@PathVariable long id){
        return employeeService.getUserOptionDeptId(id);
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
    @GetMapping("/hods")
    public ResponseEntity<List<HODFrontEnd>> fetchAllDepartmentHOD(){
        return employeeService.fetchAllHod();
    }
    @GetMapping ("/emp-data")
    public ResponseEntity<List<EmployeeData>> getEmployeeData(){
        return employeeService.getEmpData();
    }
}
