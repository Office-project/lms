package com.northwest.lms.config.security;


import com.northwest.lms.models.Employee;
import com.northwest.lms.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeDetailService implements UserDetailsService {
    private EmployeeRepository employeeRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee user = employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No Staff with " + email+" found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }

}
