package com.northwest.lms.controllers;

import com.northwest.lms.dtos.LoginDto;
import com.northwest.lms.response.Login;
import com.northwest.lms.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Login> login(@RequestBody LoginDto loginDto) throws Exception {
        return loginService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(String token) {

        return loginService.logout(token);
    }
}
