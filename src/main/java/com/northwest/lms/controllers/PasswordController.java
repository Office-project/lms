package com.northwest.lms.controllers;

import com.northwest.lms.dtos.ChangePasswordDto;
import com.northwest.lms.models.Employee;
import com.northwest.lms.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class PasswordController {
    private EmployeeService employeeService;

    @PostMapping("/change_password")
    public ResponseEntity<Employee> changePassword(@RequestBody ChangePasswordDto passwordDto){
        return employeeService.changePassword(passwordDto);
    }

    @GetMapping("/map")
    public ResponseEntity<Map<Integer,String>> getMap(){
        Map<Integer,String> maps = new HashMap<>();
        maps.put(1, "One");
        maps.put(2, "two");
        maps.put(3, "Three");
        return ResponseEntity.ok(maps);
    }


}
