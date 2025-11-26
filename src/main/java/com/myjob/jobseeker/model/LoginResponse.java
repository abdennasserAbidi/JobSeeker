package com.myjob.jobseeker.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    private long expiresIn;

    private User user;

    private String messageError;

}
