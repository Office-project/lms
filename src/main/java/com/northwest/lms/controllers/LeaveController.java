package com.northwest.lms.controllers;

import com.northwest.lms.dtos.*;
import com.northwest.lms.models.TakeLeave;
import com.northwest.lms.services.TakeLeaveService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/leave")
@AllArgsConstructor
public class LeaveController {
    private TakeLeaveService leaveService;

    @PostMapping
    public ResponseEntity<Long> applyForLeave(@RequestBody TakeLeaveDto takeLeaveDto){
        return leaveService.takeLeave(takeLeaveDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<LeaveDto> attachFile(@RequestPart MultipartFile file,@PathVariable long id){
        return leaveService.addFile(id, file);
    }

    @GetMapping
    public ResponseEntity<List<MyLeave>> getCustomLeave(){
        return leaveService.getCustomLeave();
    }

    @GetMapping("/history")
    public ResponseEntity<List<History>> getLeaveHistory(){
        return leaveService.getLeaveHistory();
    }



    @GetMapping("/relief_appoval")
    public ResponseEntity<List<LeaveDto>> checkReliefPendingApproval(){
        return leaveService.getReliefPendingApproval();
    }

    @PostMapping("/relief_approval")
    public ResponseEntity<String> reliefResponse(@RequestBody ApprovalDto approvalDto){
        return leaveService.reliefResponse(approvalDto);
    }

    @GetMapping("/hod_approval")
    public ResponseEntity<List<LeaveDto>> checkHodApproval(){
        return leaveService.getHodPendingApproval();
    }
    @PostMapping("/hod_approval")
    public ResponseEntity<String> hodResponse(@RequestBody ApprovalDto approvalDto){
        return leaveService.hodResponse(approvalDto);
    }

    @GetMapping("/admin_approval")
    public ResponseEntity<List<LeaveDto>> checkAdminApproval(){
        return leaveService.getAdminPendingApproval();
    }
    @PostMapping("/admin_approval")
    public ResponseEntity<String> adminResponse(@RequestBody ApprovalDto approvalDto){
        return leaveService.adminResponse(approvalDto);
    }
    @GetMapping("/custom_notification")
    public ResponseEntity<List<Notice>> getMyNotifications(){
        return leaveService.getNotification();
    }
}
