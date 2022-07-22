package com.northwest.lms.dtos;

import com.northwest.lms.models.Department;
import com.northwest.lms.models.Employee;
import com.northwest.lms.models.LeaveType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
@Builder@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class LeaveDto {
    private Long leaveId;
    private String applicantName;
    private String reliefOfficer;
    private Boolean reliefOfficerApproval = null;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String department;
    private Boolean hodApproval = null;
    private int days;
    private String reason;
    private String leaveDocument;
    private Boolean adminApproval = null;
    private String createdDate;
    private String status;
}
