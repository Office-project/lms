package com.northwest.lms.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserOption {
    private Long id;
    private String name;
}
