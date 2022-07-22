package com.northwest.lms.services.Impl;

import com.northwest.lms.config.mail.LmsSender;
import com.northwest.lms.dtos.*;
import com.northwest.lms.enums.Role;
import com.northwest.lms.exceptions.PasswordIncorrectException;
import com.northwest.lms.models.*;
import com.northwest.lms.repositories.*;
import com.northwest.lms.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private HeadOfDepartmentRepository hodRepo;
    private DepartmentRepository deptRepository;
    private LeaveTypeRepository leaveTypeRepository;
    private LmsSender sender;
    private LocationRepository locationRepository;
    private PasswordEncoder encoder;
    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeDto employeeDto) {
        Department dept = getDept(employeeDto.getDepartmentID());
        Employee personalSupervisor = employeeRepository.findById(employeeDto.getPersonalSupervisorId()).orElse(null);
        Employee emp = Employee.builder()
                .gender(employeeDto.getGender())
                .email(employeeDto.getEmail())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .password(encoder.encode("Northwest123"))
                .role(employeeDto.getRole())
                .joinDate(employeeDto.getJoinDate())
                .department(dept)
                .personalSupervisor(personalSupervisor)
                .location(locationRepository.findById(employeeDto.getLocationId()).orElse(null))
                .leavesApplicable(getLeaves(getLeavesId((employeeDto.getGender().name()))))
                .build();


        EmailSenderDto sendEmail = new EmailSenderDto();
        sendEmail.setTo(employeeDto.getEmail());
        sendEmail.setSubject("WELCOME TO LMS PORTAL");
        sendEmail.setContent("An account have been registered for you on The LMS Platform, you can login using this email and `Northwest123` as password.<br>" +
                "<p>Click on the link to <a href=\"http://51.77.99.34:8080/LMS\" target=\"_blank\">visit</a>. </p>");

        employeeRepository.save(emp);
        Thread thread = new Thread(()->{
            sendRegisterationMail(sendEmail);
        });

        thread.start();


        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    private List<LeaveType> getLeaves(List<Integer> leavesApplicable) {
        if(leavesApplicable == null) return null;
        if(leavesApplicable.size()==0) return null;
        List<LeaveType> myLeaves = new ArrayList<>();

        if(leavesApplicable.get(0) > 0L){
            for(int i = 0; i < leavesApplicable.size(); i++){
                myLeaves.add(leaveTypeRepository.findById(Long.valueOf(leavesApplicable.get(i))).get());
            }
        }

        return myLeaves;
    }

    private Department getDept(Long departmentID) {
        if(departmentID == null) return null;
        if(departmentID.equals(0L)) return null;
        return deptRepository.findById(departmentID).get();
    }

    @Override
    public ResponseEntity<Employee> updateEmployee(Long Id, EmployeeDto employeeDto) {
        Employee emp = employeeRepository.findById(Id).get();
       return null;
    }

    @Override
    public ResponseEntity<List<Employee>> fetchAllEmployee() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @Override
    public ResponseEntity<Employee> changePassword(ChangePasswordDto passwordDto) {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = employeeRepository.findEmployeeByEmail(loggedInUser.getUsername()).get();
        if(!encoder.matches(passwordDto.getOldPassword(),emp.getPassword()))throw new PasswordIncorrectException("Your old password is incorrect");
        if(!passwordDto.getConfirmPassword().equals(passwordDto.getNewPassword()))throw new PasswordIncorrectException("Your new password is inconsistent");
        emp.setPassword(encoder.encode(passwordDto.getNewPassword()));
        return ResponseEntity.ok(employeeRepository.save(emp));
    }

    @Override
    public ResponseEntity<Location> createLocation(LocationDto loactionDto) {
        Location location = Location.builder()
                .state(loactionDto.getState())
        .build();
        return new ResponseEntity<>(locationRepository.save(location),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<UserOption>> getUserOption() {
        List<Employee> list = employeeRepository.findAll();
        return ResponseEntity.ok(mapToUserOption(list));
    }

    @Override
    public ResponseEntity<List<LocationOption>> getLocationOption() {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(locations.stream().map(this::mapToLocationOption).collect(Collectors.toList()));
    }

    private LocationOption mapToLocationOption(Location location){
        return LocationOption.builder()
                .id(location.getId())
                .name(location.getState())
                .build();
    }

    @Override
    public ResponseEntity<List<DepartmentOption>> getDepartmentOption() {
        List<Department> departments = deptRepository.findAll();
        return ResponseEntity.ok(departments.stream().map(this::mapToDepartmentOption).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<LeaveTypeOption>> getLeaveTypeOption() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = employeeRepository.findEmployeeByEmail(loggedInUser.getUsername()).get();
        return ResponseEntity.ok(emp.getLeavesApplicable().stream().map(this::mapToLeaveOption).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<Location>> findAllLocation() {
        return ResponseEntity.ok(locationRepository.findAll());
    }

    @Override
    public ResponseEntity<List<EmployeeData>> getEmpData() {
        return ResponseEntity.ok(employeeRepository.findAll().stream().map(this::mapToEmpData).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<UserOption>> getUserOptions() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = employeeRepository.findEmployeeByEmail(loggedInUser.getUsername()).get();
        Department dep = emp.getDepartment();
        if(dep == null) return getUserOption();
        List<Employee> empList = employeeRepository.findEmployeesByDepartment(dep);
        return ResponseEntity.ok(mapToUserOption(empList));
    }

    @Override
    public ResponseEntity<List<HODFrontEnd>> fetchAllHod() {
        return ResponseEntity.ok(hodRepo.findAll().stream().map(this::toHodFrontEnd).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<UserOption>> getUserOptionDeptId(long id) {
        Department dep = deptRepository.findById(id).get();
        List<Employee> empList = employeeRepository.findEmployeesByDepartment(dep);
        return ResponseEntity.ok(mapToUserOption(empList));
    }

    private HODFrontEnd toHodFrontEnd(HeadOfDepartments hod){
        String emp = null;
        if(hod.getEmployee() != null){
            emp = hod.getEmployee().getFirstName()+" "+hod.getEmployee().getLastName();
        }
        return HODFrontEnd.builder()
                .department(hod.getDepartment().getDepartmentName())
                .employee(emp)
                .id(hod.getHodId())
                .deptId(hod.getDepartment().getDeptId())
                .build();
    }

    private EmployeeData mapToEmpData(Employee employee) {
        return EmployeeData.builder()
                .id(employee.getEmpId())
                .firstName(employee.getFirstName())
                .department(getDepartment(employee.getDepartment()))
                .email(employee.getEmail())
                .gender(employee.getGender().name())
                .location(getLocation(employee.getLocation()))
                .joinDate(employee.getJoinDate().toString())
                .lastName(employee.getLastName())
                .role(employee.getRole().name())
                .build();
    }

    private String getLocation(Location location) {
        if(location == null) return null;
        return location.getState();
    }

    private String getDepartment(Department department) {
        if(department == null) return null;
        return department.getDepartmentName();
    }

    private LeaveTypeOption mapToLeaveOption(LeaveType leaveType) {
        return LeaveTypeOption.builder()
                .id(leaveType.getLeaveId())
                .name(leaveType.getLeaveName())
                .build();
    }

    private DepartmentOption mapToDepartmentOption(Department department){
        return DepartmentOption.builder()
                .id(department.getDeptId())
                .name(department.getDepartmentName())
                .build();
    }

    private List<UserOption> mapToUserOption(List<Employee> list) {
        List<UserOption> userOptionList = new ArrayList<>();
        for (Employee e: list){
            userOptionList.add(new UserOption(e.getEmpId(),e.getFirstName()+" "+e.getLastName()));
        }
        return userOptionList;
    }

    public void sendRegisterationMail(EmailSenderDto dto){
        sender.send(dto);
    }

    private Role getRole(String role){
        if(role.equalsIgnoreCase("ADMIN")) return Role.ADMIN;
        return Role.STAFF;
    }

    private ArrayList getLeavesId(String gender){
        if(gender.equals("MALE")) return new ArrayList(asList(1,2,4));
        else return new ArrayList(asList(1,2,3,4));
    }
}
