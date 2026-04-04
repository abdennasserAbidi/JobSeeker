package com.myjob.jobseeker.model;

import java.util.ArrayList;
import java.util.List;

import com.myjob.jobseeker.model.datalist.Company;

import lombok.Data;

@Data
public class Companies {
    private List<Company> companies = new ArrayList<>();
}
