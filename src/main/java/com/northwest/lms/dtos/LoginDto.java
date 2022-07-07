package com.northwest.lms.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public
class LoginDto{
    String email;
    String password;
}
