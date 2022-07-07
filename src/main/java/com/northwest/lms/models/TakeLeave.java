package com.northwest.lms.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TakeLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    @ManyToOne
    @JoinColumn(name = "employee_emp_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "relief_officer_emp_id")
    private Employee reliefOfficer;
    private Boolean reliefOfficerApproval = null;
    @ManyToOne
    @JoinColumn(name = "leave_type_leave_id")
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "department_dept_id")
    private Department department;
    private Boolean hodApproval = null;
    private int days;
    private String reasonForRequest;
    private String reasonForDecline;
    private String reasonForCancellation;
    private String leaveDocument;
    private Boolean adminApproval = null;

    @CreationTimestamp
    private LocalDate createdDate;
}
