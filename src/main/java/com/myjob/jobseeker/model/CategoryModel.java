package com.myjob.jobseeker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CategoryModel {
    private List<ServiceCategory> listCategories = new ArrayList<>();
    private List<FreelanceService> listService = new ArrayList<>();
    private List<FreelanceSector> listSector = new ArrayList<>();
}
