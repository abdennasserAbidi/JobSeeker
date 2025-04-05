package com.myjob.jobseeker.repo;

import java.util.List;

import com.myjob.jobseeker.dtos.Criteria;
import com.myjob.jobseeker.model.User;

public interface CriteriaRepository {

    List<User> searchUsers(Criteria request);

}
