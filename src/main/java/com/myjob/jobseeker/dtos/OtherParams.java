package com.myjob.jobseeker.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OtherParams {

    private int id;
    private List<String> companies;
    private List<String> universities;

}
