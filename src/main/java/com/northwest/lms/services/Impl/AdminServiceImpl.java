package com.northwest.lms.services.Impl;

import com.northwest.lms.dtos.DepartmentDto;
import com.northwest.lms.dtos.HeadOfDepartmentsDto;
import com.northwest.lms.dtos.LeaveTypeDto;
import com.northwest.lms.dtos.UpdateHod;
import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import com.northwest.lms.models.HeadOfDepartments;
import com.northwest.lms.models.LeaveType;
import com.northwest.lms.repositories.DepartmentRepository;
import com.northwest.lms.repositories.EmployeeRepository;
import com.northwest.lms.repositories.HeadOfDepartmentRepository;
import com.northwest.lms.repositories.LeaveTypeRepository;
import com.northwest.lms.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepo;
    private HeadOfDepartmentRepository hodRepo;
    private EmployeeRepository employeeRepo;

    private LeaveTypeRepository leaveTypeRepository;

    @Override
    public ResponseEntity<Department> createDepartment(DepartmentDto departmentDto) {
        Department department = Department.builder()
                .DepartmentName(departmentDto.getDepartmentName())
                .build();
        return new ResponseEntity<>(departmentRepo.save(department), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HeadOfDepartments> assignHOD(HeadOfDepartmentsDto hodDto) {
        Department department = departmentRepo.findById(hodDto.getDepartmentId()).get();
        Employee emp = employeeRepo.findById(hodDto.getEmployeeId()).get();

        HeadOfDepartments hod = HeadOfDepartments.builder()
                .department(department)
                .employee(emp)
                .build();

        return new ResponseEntity<>(hodRepo.save(hod), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<HeadOfDepartments>> fetchAllHods() {
        return ResponseEntity.ok(hodRepo.findAll());
    }

    @Override
    public ResponseEntity<HeadOfDepartments> updateHOD(HeadOfDepartmentsDto headOfDepartmentsDto,Long hodId) {
        return null;
    }

    @Override
    public ResponseEntity<LeaveType> createLeaveType(LeaveTypeDto leaveTypeDto) {
        LeaveType leaveType = LeaveType.builder()
                .leaveName(leaveTypeDto.getLeaveName())
                .daysAllowed(leaveTypeDto.getDaysAllowed())
                .build();
        return new ResponseEntity<>(leaveTypeRepository.save(leaveType),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<LeaveType>> fetchAllLeaveTypes() {
        return ResponseEntity.ok(leaveTypeRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentRepo.findAll());
    }

    @Override
    public ResponseEntity<HeadOfDepartments> updateHod(UpdateHod updateHod) {

        HeadOfDepartments hod = hodRepo.findById(updateHod.getHodId()).get();
        Employee emp = employeeRepo.findById(updateHod.getEmpId()).get();

        hod.setEmployee(emp);
        return ResponseEntity.ok(hodRepo.save(hod));
    }
}
