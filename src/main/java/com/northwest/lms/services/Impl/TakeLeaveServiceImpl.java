package com.northwest.lms.services.Impl;

import com.northwest.lms.dtos.*;
import com.northwest.lms.exceptions.FileException;
import com.northwest.lms.exceptions.NotEligibleException;
import com.northwest.lms.models.*;
import com.northwest.lms.repositories.EmployeeRepository;
import com.northwest.lms.repositories.HeadOfDepartmentRepository;
import com.northwest.lms.repositories.LeaveTypeRepository;
import com.northwest.lms.repositories.TakeLeaveRepository;
import com.northwest.lms.services.TakeLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class TakeLeaveServiceImpl implements TakeLeaveService {
    @Autowired private EmployeeRepository empRepo;
    @Autowired private HeadOfDepartmentRepository hodRepo;
    @Autowired private LeaveTypeRepository leaveTypeRepo;
    @Autowired private TakeLeaveRepository takeLeaveRepo;

    private LocalDate now = LocalDate.now();
    private LocalDate yearStart = now.with(firstDayOfYear());
    private LocalDate yearEnd = now.with(lastDayOfYear());

    private final String saveDirectory = "C:\\lms";

    @Override
    public ResponseEntity<Long> takeLeave(TakeLeaveDto takeLeaveDto) {

        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = empRepo.findEmployeeByEmail(loggedInUser.getUsername()).get();
        Employee reliefOfficer = empRepo.findById(takeLeaveDto.getReliefOfficerId()).get();
        LeaveType type = leaveTypeRepo.findById(takeLeaveDto.getLeaveTypeId()).get();
        System.out.println(takeLeaveDto);

        if(!criteriaOne(emp.getJoinDate())) throw new NotEligibleException("Not Eligible");
        if(!criteriaTwo(emp.getDepartment(),takeLeaveDto.getStartDate(),takeLeaveDto.getEndDate())) throw new NotEligibleException("Not Eligible");
        TakeLeave takeLeave = TakeLeave.builder()
                        .employee(emp)
                .reliefOfficer(reliefOfficer)
                .department(emp.getDepartment())
                .startDate(takeLeaveDto.getStartDate())
                .leaveType(type)
                .endDate(takeLeaveDto.getEndDate())
                .days((int)getDays(takeLeaveDto.getStartDate(),takeLeaveDto.getEndDate()))
                .reasonForRequest(takeLeaveDto.getReason())
                .build();

        if(takeLeave.getDays() < 1 ) throw new NotEligibleException("Leave must be up to a 1 day");
        if(takeLeave.getDays() < type.getDaysAllowed()) throw new NotEligibleException("Leave cannot be split");
        if(takeLeave.getDays() > type.getDaysAllowed()) throw new NotEligibleException("Days is above the policy days permitted");


        return new ResponseEntity<>(takeLeaveRepo.save(takeLeave).getLeaveId(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<LeaveDto>> getReliefPendingApproval() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = empRepo.findEmployeeByEmail(loggedInUser.getUsername()).get();
        List<TakeLeave> myAwaitingApprovals = takeLeaveRepo.findTakeLeavesByReliefOfficerAndReliefOfficerApproval(emp,null);
        List<LeaveDto> awaitingApprovalDto = myAwaitingApprovals.stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(awaitingApprovalDto);
    }

    private String getFile(MultipartFile inputFile){
        double random = Math.random();
        String randomNumber = Double.toString(random);
        String result;
        if (!inputFile.isEmpty()) {
            try {
                String originalFilename = inputFile.getOriginalFilename();
                File destinationFile = new File(saveDirectory + File.separator + originalFilename);
                if (destinationFile.exists()) {
                    String originalFilename2 = randomNumber.concat(Objects.requireNonNull(inputFile.getOriginalFilename()));
                    destinationFile = new File(saveDirectory + File.separator + originalFilename2);
                }
                inputFile.transferTo(destinationFile);
                result = destinationFile.toString();

            } catch (Exception e) {
                System.out.println("Erorr at the exception level");
                throw new FileException("File pass error");
            }
        } else {
            throw new FileException("File Not found");
        }

        return result;
    }

    @Override
    public ResponseEntity<List<LeaveDto>> getHodPendingApproval() {
        List<TakeLeave> all = takeLeaveRepo.findTakeLeavesByReliefOfficerApprovalAndHodApproval(Boolean.TRUE,null);
        return ResponseEntity.ok(all.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<LeaveDto>> getAdminPendingApproval() {
        List<TakeLeave> all = takeLeaveRepo.findTakeLeavesByHodApprovalAndAdminApproval(Boolean.TRUE,null);
        return ResponseEntity.ok(all.stream().map(this::mapToDto).collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<String> reliefResponse(ApprovalDto approvalDto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(approvalDto.getId()).get();
        takeLeave.setReliefOfficerApproval(approvalDto.getStatus());
        return ResponseEntity.ok("Noted");
    }

    @Override
    public ResponseEntity<String> hodResponse(ApprovalDto approvalDto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(approvalDto.getId()).get();
        takeLeave.setHodApproval(approvalDto.getStatus());
        return ResponseEntity.ok("Noted");
    }

    @Override
    public ResponseEntity<String> adminResponse(ApprovalDto approvalDto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(approvalDto.getId()).get();
        takeLeave.setAdminApproval(approvalDto.getStatus());
        return ResponseEntity.ok("Noted");
    }

    @Override
    public ResponseEntity<List<MyLeave>> getCustomLeave() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = empRepo.findEmployeeByEmail(loggedInUser.getUsername()).get();
        List<LeaveType> lt = emp.getLeavesApplicable();
        List<MyLeave> myCustomLeaves = new ArrayList<>();
        for (LeaveType leaveType : lt) {
            MyLeave custom = new MyLeave();
            custom.setId(leaveType.getLeaveId());
            custom.setName(leaveType.getLeaveName());
            custom.setDuration(leaveType.getDaysAllowed());
            custom.setEligible(criteriaOne(emp.getJoinDate()));
            myCustomLeaves.add(custom);
        }

        return ResponseEntity.ok(myCustomLeaves);
    }

    @Override
    public ResponseEntity<List<History>> getLeaveHistory() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = empRepo.findEmployeeByEmail(loggedInUser.getUsername()).get();
        List<TakeLeave> myLeaves = takeLeaveRepo.findTakeLeavesByEmployee(emp);
        return ResponseEntity.ok(myLeaves.stream().map(this::mapToHistory).collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<LeaveDto> addFile(Long id, MultipartFile file) {
        String document = getFile(file);
        TakeLeave leave = takeLeaveRepo.findById(id).orElse(null);
        assert leave != null;
        leave.setLeaveDocument(document);
        takeLeaveRepo.save(leave);
        return ResponseEntity.ok(mapToDto(leave));
    }

    @Override
    public ResponseEntity<Map<String, Notice>> getNotices() {
        return null;
    }

    private History mapToHistory(TakeLeave leave) {
        return   History.builder()
                .duration(leave.getDays())
                .start(leave.getStartDate())
                .end(leave.getEndDate())
                .reasonForRequest(leave.getReasonForRequest())
                .reasonForDecline(leave.getReasonForDecline())
                .reasonForCancellation(leave.getReasonForCancellation())
                .reliefOfficer(leave.getReliefOfficer().getFirstName()+" "+leave.getReliefOfficer().getLastName())
                .appliedOn(leave.getCreatedDate())
                .download(leave.getLeaveDocument())
                .build();
    }


    private boolean criteriaTwo(Department department, LocalDate startDate, LocalDate endDate) {
        List<TakeLeave> leaves = takeLeaveRepo.findTakeLeavesByDepartmentAndCreatedDateBetween(department,yearStart,yearEnd);
        if(leaves.size() == 0) return true;

        for(TakeLeave e: leaves){
            if((e.getStartDate().isBefore(endDate) && e.getEndDate().isAfter(startDate))
            || (e.getStartDate().isAfter(startDate) && e.getStartDate().isBefore(endDate))
            ){
                return false;
            }
        }
        return true;
    }

    private long getDays(LocalDate start, LocalDate end){
        return DAYS.between(start, end);
    }

    private boolean criteriaOne(LocalDate joinDate){
        long days = DAYS.between(joinDate,now);
        return days >= 365;
    }


    private LeaveDto mapToDto(TakeLeave takeLeave){
        return LeaveDto.builder()
                .leaveId(takeLeave.getLeaveId())
                .applicantName(takeLeave.getEmployee().getFirstName() +" " +takeLeave.getEmployee().getLastName())
                .reliefOfficer(takeLeave.getReliefOfficer().getFirstName()+" "+takeLeave.getReliefOfficer().getLastName())
                .reliefOfficerApproval(takeLeave.getReliefOfficerApproval())
                .leaveType(takeLeave.getLeaveType().getLeaveName())
                .startDate(takeLeave.getStartDate())
                .endDate(takeLeave.getEndDate())
                .department(takeLeave.getDepartment().getDepartmentName())
                .hodApproval(takeLeave.getHodApproval())
                .days(takeLeave.getDays())
                .reason(takeLeave.getReasonForRequest())
                .leaveDocument(takeLeave.getLeaveDocument())
                .adminApproval(takeLeave.getAdminApproval())
                .createdDate(takeLeave.getCreatedDate())
                .build();
    }






}
