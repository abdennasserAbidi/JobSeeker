package com.myjob.jobseeker.model.field;

import java.util.ArrayList;
import java.util.List;
import com.myjob.jobseeker.model.datalist.Field;
import lombok.Data;

@Data
public class Fields {

    private List<Field> studyfields = new ArrayList<>();

}
