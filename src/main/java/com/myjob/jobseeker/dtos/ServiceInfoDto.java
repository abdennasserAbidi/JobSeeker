package com.myjob.jobseeker.dtos;

import lombok.Data;
import java.util.List;

import com.myjob.jobseeker.model.ServiceCategory;

@Data
public class ServiceInfoDto {
    private int id;
    private String userServiceName;
    private ServiceCategory category;
    private String otherCategory;
    private String bio;
    private String email;
    private String country;
    private String city;
    private List<String> phoneList;
    private List<String> addressList;
}