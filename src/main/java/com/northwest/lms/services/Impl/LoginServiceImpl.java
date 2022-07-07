package com.northwest.lms.services.Impl;

import com.northwest.lms.config.security.JwtTokenProvider;
import com.northwest.lms.dtos.JwtAuthResponse;
import com.northwest.lms.dtos.LoginDto;
import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import com.northwest.lms.models.HeadOfDepartments;
import com.northwest.lms.repositories.BlackListedTokenRepository;
import com.northwest.lms.repositories.EmployeeRepository;
import com.northwest.lms.repositories.HeadOfDepartmentRepository;
import com.northwest.lms.response.Login;
import com.northwest.lms.services.BlackListService;
import com.northwest.lms.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse httpServletResponse;
    private final BlackListService blackListService;
    private final BlackListedTokenRepository blackListedTokenRepository;
    private final HttpServletRequest httpServletRequest;

    private final EmployeeRepository employeeRepository;

    private final HeadOfDepartmentRepository hodRepo;

    @Override
    public ResponseEntity<Login> login(LoginDto loginDto) throws Exception {
        Authentication authentication ;
        String token;
        Employee employee = null;

        try{
            Authentication auth =  new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),loginDto.getPassword());

            authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenProvider.generateToken(authentication);
            httpServletResponse.setHeader("Authorization", token);
            employee = employeeRepository.findEmployeeByEmail(loginDto.getEmail()).get();

        }
        catch (
                BadCredentialsException ex){
            throw new Exception("incorrect user credentials", ex);

        }
        System.out.println(new JwtAuthResponse(token));
        String initial = employee.getFirstName().toUpperCase().charAt(0)+"."+employee.getLastName().toUpperCase().charAt(0);

        Login login = Login.builder()
                .id(employee.getEmpId())
                .token(token)
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .role(employee.getRole().toString())
                .department(employee.getDepartment().getDepartmentName())
                .initail(initial)
                .location(employee.getLocation().getState()+" "+employee.getLocation().getOffice())
                .joinDate(employee.getJoinDate())
                .supervisor(getSupervisor(employee))
                .build();
        return ResponseEntity.ok(login);
    }
    @Override
    public ResponseEntity<?> logout(String token) {
        token = httpServletRequest.getHeader("Authorization");
        blackListService.blackListToken(token);
        return new ResponseEntity<>("Logout Successful", HttpStatus.OK);

    }

    public String getSupervisor(Employee emp){
        if(emp.getDepartment() == null) return null;
        Department dept = emp.getDepartment();

        HeadOfDepartments hod = hodRepo.findHeadOfDepartmentsByDepartment(dept);
        if(hod == null) return null;

        return hod.getEmployee().getFirstName()+" "+hod.getEmployee().getLastName();
    }


}

