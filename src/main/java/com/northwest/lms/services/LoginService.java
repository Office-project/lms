package com.northwest.lms.services;



import com.northwest.lms.dtos.Dashboard;
import com.northwest.lms.dtos.LoginDto;
import com.northwest.lms.response.Login;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<Login> login(LoginDto loginDto) throws Exception;
    ResponseEntity<?> logout(String token);
    ResponseEntity<Dashboard> getDashboard();

}
