package com.northwest.lms.services;


import com.northwest.lms.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TakeLeaveService {
    ResponseEntity<Long> takeLeave(TakeLeaveDto takeLeaveDto);
    ResponseEntity<String> supervisor(long id);
    ResponseEntity<String> supervisor(long id, CancelDto cdto);

    ResponseEntity<String> reliefResponse(long id);
    ResponseEntity<String> reliefResponse(long id, CancelDto cdto);

    ResponseEntity<String> hodResponse(long id);
    ResponseEntity<String> hodResponse(long id, CancelDto cdto);

    ResponseEntity<String> adminResponse(long id);
    ResponseEntity<String> adminResponse(long id, CancelDto cdto);

    ResponseEntity<List<MyLeave>> getCustomLeave();

    ResponseEntity<List<History>> getLeaveHistory();

    ResponseEntity<LeaveDto> addFile(Long id, MultipartFile file);
    ResponseEntity<List<Notice>> getNotification();


}
