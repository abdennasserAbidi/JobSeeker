package com.myjob.jobseeker.model.datalist;

import lombok.Data;

import java.util.List;

@Data
public class City {

    String name;
    List<Delegations> delegations;

}
