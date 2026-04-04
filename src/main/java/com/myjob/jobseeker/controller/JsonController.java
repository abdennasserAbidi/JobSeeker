package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.model.CompanyModel;
import com.myjob.jobseeker.model.activity.ActivityModel;
import com.myjob.jobseeker.model.field.FieldModel;
import com.myjob.jobseeker.model.institute.InstituteModel;
import com.myjob.jobseeker.services.JsonService;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class JsonController {

    private final JsonService jsonService;

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyModel>> getListCompanies() {
        return ResponseEntity.ok(jsonService.getListCompanies());
    }

    @PostMapping("/saveCompany")
    public ResponseEntity<String> saveCompany(@RequestParam String companyName) {

        CompanyModel companyModel = new CompanyModel();
        companyModel.setName(companyName);        

        return ResponseEntity.ok(jsonService.saveCompany(companyModel));
    }

    @GetMapping("/getAllInstitutes")
    public ResponseEntity<List<InstituteModel>> getAllInstitutes() {
        return ResponseEntity.ok(jsonService.getAllInstitutes());
    }

    @PostMapping("/saveInstitute")
    public ResponseEntity<String> saveInstitute(@RequestParam String schoolName) {

        InstituteModel instituteModel = new InstituteModel();
        instituteModel.setName(schoolName);        

        return ResponseEntity.ok(jsonService.saveInstitute(instituteModel));
    }

    @GetMapping("/getAllActivities")
    public ResponseEntity<List<ActivityModel>> getAllActivities() {
        return ResponseEntity.ok(jsonService.getAllActivities());
    }

    @PostMapping("/saveActivity")
    public ResponseEntity<String> saveActivity(@RequestParam String activityName) {

        ActivityModel activityModel = new ActivityModel();
        activityModel.setName(activityName);        

        return ResponseEntity.ok(jsonService.saveActivity(activityModel));
    }

    @GetMapping("/getAllFields")
    public ResponseEntity<List<FieldModel>> getAllFields() {
        return ResponseEntity.ok(jsonService.getAllFields());
    }

    @PostMapping("/saveField")
    public ResponseEntity<String> saveField(@RequestParam String fieldName) {

        FieldModel activityModel = new FieldModel();
        activityModel.setName(fieldName);        

        return ResponseEntity.ok(jsonService.saveField(activityModel));
    }

}
