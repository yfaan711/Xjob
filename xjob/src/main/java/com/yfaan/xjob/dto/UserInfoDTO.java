package com.yfaan.xjob.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserInfoDTO {
    private Long userId;
    private String city;
    private String introduce;
    private Boolean gender;
    private LocalDate birthday;
}