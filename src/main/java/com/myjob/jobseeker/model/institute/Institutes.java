package com.myjob.jobseeker.model.institute;

import java.util.ArrayList;
import java.util.List;
import com.myjob.jobseeker.model.datalist.School;
import lombok.Data;

@Data
public class Institutes {
    private List<School> school = new ArrayList<>();
}
