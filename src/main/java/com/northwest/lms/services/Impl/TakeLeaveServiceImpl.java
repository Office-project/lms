package com.northwest.lms.services.Impl;

import com.northwest.lms.dtos.*;
import com.northwest.lms.enums.Role;
import com.northwest.lms.exceptions.BadCredentialsException;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
        LocalDate endDate = getEndDate(takeLeaveDto.getStartDate(),type.getDaysAllowed());

        if(!criteriaOne(emp.getJoinDate())) throw new NotEligibleException("Not Eligible");
        personalCriteria(emp,type);
        if(!criteriaTwo(emp.getDepartment(),takeLeaveDto.getStartDate(),endDate)) throw new NotEligibleException("Not Eligible");
        TakeLeave takeLeave = TakeLeave.builder()
                        .employee(emp)
                .reliefOfficer(reliefOfficer)
                .department(emp.getDepartment())
                .startDate(takeLeaveDto.getStartDate())
                .leaveType(type)
                .endDate(endDate)
                .days(crossCheckDuration(takeLeaveDto.getStartDate(),endDate,type.getDaysAllowed()))
                .reasonForRequest(type.getLeaveName())
                .build();

        if(takeLeave.getDays() < type.getDaysAllowed()) throw new NotEligibleException("Leave cannot be split");
        if(takeLeave.getDays() > type.getDaysAllowed()) throw new NotEligibleException("Days is above the policy days permitted");


        return new ResponseEntity<>(takeLeaveRepo.save(takeLeave).getLeaveId(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> supervisor(long id) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setSupervisorApproval(Boolean.TRUE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> supervisor(long id, CancelDto cdto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setSupervisorApproval(Boolean.FALSE);
        takeLeave.setReasonForDecline(cdto.getMessage());
        takeLeave.setDecision(Boolean.FALSE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> reliefResponse(long id) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setReliefOfficerApproval(Boolean.TRUE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> reliefResponse(long id, CancelDto cdto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setReliefOfficerApproval(Boolean.FALSE);
        takeLeave.setReasonForDecline(cdto.getMessage());
        takeLeave.setDecision(Boolean.FALSE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> hodResponse(long id) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setHodApproval(Boolean.TRUE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> hodResponse(long id, CancelDto cdto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setHodApproval(Boolean.FALSE);
        takeLeave.setReasonForDecline(cdto.getMessage());
        takeLeave.setDecision(Boolean.FALSE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> adminResponse(long id) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setAdminApproval(Boolean.TRUE);
        takeLeave.setDecision(Boolean.TRUE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<String> adminResponse(long id, CancelDto cdto) {
        TakeLeave takeLeave = takeLeaveRepo.findById(id).orElseThrow(() -> new BadCredentialsException("Does not Exist"));
        takeLeave.setAdminApproval(Boolean.FALSE);
        takeLeave.setReasonForDecline(cdto.getMessage());
        takeLeave.setDecision(Boolean.FALSE);
        takeLeaveRepo.save(takeLeave);
        return ResponseEntity.ok("Success");
    }

    private  LocalDate getEndDate(LocalDate startDate, int duration){
        Date start = Date.from(Instant.from(startDate.atStartOfDay(ZoneId.systemDefault())));
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);
        int days = 0;
        if(Calendar.MONDAY != cal1.get(Calendar.DAY_OF_WEEK)) throw new RuntimeException("not valid");

        if(duration < 30){
            while(days < duration) {
                cal1.add(Calendar.DATE, 1);
                if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
                        && (Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                    days++;
                }
            }
        }else {
            cal1.add(Calendar.DATE, duration);
        }
        Date end = cal1.getTime();
        return end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void personalCriteria(Employee emp, LeaveType type) {
        List<TakeLeave> leaves = takeLeaveRepo.findTakeLeavesByEmployeeAndCreatedDateBetween(emp,yearStart,yearEnd);

        for(TakeLeave e: leaves){
            if(e.getLeaveType().equals(type) && e.getDecision() == null){
                throw new NotEligibleException("You have a pending leave");
            }
        }
    }


    private int crossCheckDuration(LocalDate startDate, LocalDate endDate, int days){
        Date start = Date.from(Instant.from(startDate.atStartOfDay(ZoneId.systemDefault())));
        Date end = Date.from(Instant.from(endDate.atStartOfDay(ZoneId.systemDefault())));

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(start);
        cal2.setTime(end);

        int numberOfDays = 0;
        if(days < 30) {
            while (cal1.before(cal2)) {
                if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
                        && (Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                    numberOfDays++;
                }
                cal1.add(Calendar.DATE, 1);
            }
        }else{
            while (cal1.before(cal2)) {
                numberOfDays++;
                cal1.add(Calendar.DATE,1);
            }
        }
        return numberOfDays;
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
                System.out.println("Error at the exception level");
                throw new FileException("File pass error");
            }
        } else {
            throw new FileException("File Not found");
        }

        return result;
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
        String[] arr = document.split("\\\\");
        String fileName = arr[arr.length - 1];
        System.out.println(fileName);


        System.out.println("for filepath "+document);
        System.out.println("for file name "+fileName);



        TakeLeave leave = takeLeaveRepo.findById(id).orElse(null);
        assert leave != null;
        leave.setLeaveDocument(document);
        leave.setFile(fileName);
        takeLeaveRepo.save(leave);
        return ResponseEntity.ok(mapToDto(leave));
    }

    @Override
    public ResponseEntity<List<Notice>> getNotification() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = empRepo.findEmployeeByEmail(loggedInUser.getUsername()).get();

        boolean isHod = false;
        boolean isAdmin = false;
        HeadOfDepartments hodep =  hodRepo.findHeadOfDepartmentsByDepartment(emp.getDepartment());
        if(hodep != null){
            if(hodep.getEmployee().equals(emp))isHod = true;
        }
        if(emp.getRole().equals(Role.ADMIN)) isAdmin = true;

        List<TakeLeave> relief = takeLeaveRepo.findTakeLeavesByReliefOfficerAndReliefOfficerApproval(emp,null);
        List<TakeLeave> supervisor = takeLeaveRepo.findTakeLeavesBySupervisorAndReliefOfficerApproval(emp,true);
        List<TakeLeave> hod = takeLeaveRepo.findTakeLeavesByReliefOfficerApprovalAndHodApproval(true,null);
        List<TakeLeave> admin = takeLeaveRepo.findTakeLeavesByHodApprovalAndAdminApproval(true,null);
        List<Notice> myNotices = new ArrayList<>();

        toNotice(myNotices,relief,"ReliefOfficer");
        toNotice(myNotices,supervisor,"Supervisor");
        if(isHod) toNotice(myNotices,hod, "HeadOfDepartment");
        if(isAdmin)toNotice(myNotices,admin,"Admin");
        return ResponseEntity.ok(myNotices);
    }

    private void toNotice(List<Notice> noticeList, List<TakeLeave> leaves, String position){
        for(TakeLeave e: leaves){
            Notice notice = Notice.builder()
                    .id(e.getLeaveId())
                    .document(e.getFile())
                    .reason(e.getReasonForRequest())
                    .position(position)
                    .startDate(e.getStartDate())
                    .resumptionDate(e.getEndDate())
                    .build();
            noticeList.add(notice);
        }
    }

    private History mapToHistory(TakeLeave leave) {
        return   History.builder()
                .type(leave.getLeaveType().getLeaveName())
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
