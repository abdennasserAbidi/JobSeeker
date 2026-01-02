package com.myjob.jobseeker.repo;

import com.myjob.jobseeker.dtos.Criteria;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.announces.AnnounceModel;

import java.util.List;

public interface CriteriaRepository {

    List<User> searchUsers(Criteria request);
    List<AnnounceModel> getComments(int idAnnounce);

}
