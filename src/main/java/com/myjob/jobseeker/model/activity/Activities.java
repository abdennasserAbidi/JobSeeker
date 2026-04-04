package com.myjob.jobseeker.model.activity;

import java.util.ArrayList;
import java.util.List;
import com.myjob.jobseeker.model.datalist.Subject;

import lombok.Data;

@Data
public class Activities {
    private List<Subject> subject = new ArrayList<>();
}

