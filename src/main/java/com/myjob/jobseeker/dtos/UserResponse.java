package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.User;
import lombok.Data;

@Data
public class UserResponse {

    private String message;
    private User user;

    @Override
    public String toString() {
        return "UserResponse [message=" + message + ", user=" + user + "]";
    }

}
