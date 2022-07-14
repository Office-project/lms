package com.northwest.lms.repositories;

import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import com.northwest.lms.models.TakeLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TakeLeaveRepository extends JpaRepository<TakeLeave, Long> {
    List<TakeLeave> findTakeLeavesByDepartmentAndCreatedDateBetween(Department department, LocalDate from, LocalDate to);
    List<TakeLeave> findTakeLeavesByEmployeeAndCreatedDateBetween(Employee emp, LocalDate from, LocalDate to);
    List<TakeLeave> findTakeLeavesByEmployee(Employee emp);
    List<TakeLeave> findTakeLeavesBySupervisorAndReliefOfficerApproval(Employee emp,Boolean bool);
    List<TakeLeave> findTakeLeavesByReliefOfficerAndReliefOfficerApproval(Employee reliefOfficer, Boolean bool);
    //List<TakeLeave> findTakeLeavesByDepartmentAndHAndHodApproval(Department department, Boolean bool);
    List<TakeLeave> findTakeLeavesByReliefOfficerApprovalAndHodApproval(Boolean bool, Boolean bool2);
    List<TakeLeave> findTakeLeavesByHodApprovalAndAdminApproval(Boolean bool,Boolean bool2);
}

//Notice "things that need my approval"
//1 - someone's relief officer
//between - someone's supervisor
//2 - someone's hod
//3 - if I am admin, if I am HR


