package com.northwest.lms.controllers;

import com.northwest.lms.dtos.CancelDto;
import com.northwest.lms.dtos.Dummy;
import com.northwest.lms.services.TakeLeaveService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/actions")
public class NotificationController {
    private TakeLeaveService leaveService;

    @PostMapping("/Admin/{id}")
    public ResponseEntity<String> acceptNotificationAdmin(@PathVariable long id, @RequestBody Dummy d){
        return leaveService.adminResponse(id);
    }
    @DeleteMapping("/Admin/{id}")
    public ResponseEntity<String> acceptNotificationAdmin(@PathVariable long id, @RequestBody CancelDto cdto){
        return leaveService.adminResponse(id,cdto);
    }
    @PostMapping("/HeadOfDepartment/{id}")
    public ResponseEntity<String> acceptNotificationHod(@PathVariable long id, @RequestBody Dummy d){
        return leaveService.hodResponse(id);
    }
    @DeleteMapping("/HeadOfDepartment/{id}")
    public ResponseEntity<String> acceptNotificationHod(@PathVariable long id, @RequestBody CancelDto cdto){
        return leaveService.hodResponse(id,cdto);
    }
    @PostMapping("/Supervisor/{id}")
    public ResponseEntity<String> acceptNotificationSuper(@PathVariable long id, @RequestBody Dummy d){
        return leaveService.supervisor(id);
    }
    @DeleteMapping("/Supervisor/{id}")
    public ResponseEntity<String> acceptNotificationSuper(@PathVariable long id, @RequestBody CancelDto cdto){
        return leaveService.supervisor(id,cdto);
    }
    @PostMapping("/ReliefOfficer/{id}")
    public ResponseEntity<String> acceptNotification(@PathVariable long id,@RequestBody Dummy d){
        return leaveService.reliefResponse(id);
    }
    @DeleteMapping("/ReliefOfficer/{id}")
    public ResponseEntity<String> acceptNotification(@PathVariable long id, @RequestBody CancelDto cdto){
        return leaveService.reliefResponse(id,cdto);
    }

}
