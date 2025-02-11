package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.User;

public class UserResponse {

    private String message;
    private User user;
    
    @Override
    public String toString() {
        return "UserResponse [message=" + message + ", user=" + user + "]";
    }

    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    

}
