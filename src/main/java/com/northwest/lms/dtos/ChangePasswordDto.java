package com.northwest.lms.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto{
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
