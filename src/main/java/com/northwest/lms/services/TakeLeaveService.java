package com.northwest.lms.services;


import com.northwest.lms.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TakeLeaveService {
    ResponseEntity<Long> takeLeave(TakeLeaveDto takeLeaveDto);

    ResponseEntity<List<LeaveDto>> getReliefPendingApproval();

    ResponseEntity<List<LeaveDto>> getHodPendingApproval();

    ResponseEntity<List<LeaveDto>> getAdminPendingApproval();

    ResponseEntity<String> reliefResponse(ApprovalDto approvalDto);

    ResponseEntity<String> hodResponse(ApprovalDto approvalDto);

    ResponseEntity<String> adminResponse(ApprovalDto approvalDto);

    ResponseEntity<List<MyLeave>> getCustomLeave();

    ResponseEntity<List<History>> getLeaveHistory();

    ResponseEntity<LeaveDto> addFile(Long id, MultipartFile file);

    ResponseEntity<Map<String, Notice>> getNotices();
}
