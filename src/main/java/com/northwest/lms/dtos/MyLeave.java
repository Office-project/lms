package com.northwest.lms.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyLeave {
    private long id;
    private String name;
    private int duration;
    private boolean eligible;
}
