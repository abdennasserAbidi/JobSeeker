package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.*;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IUserService {
    void saveProfessionalInfo(ProfessionalStatus professionalStatus);
    void saveSkillsInfo(CandidateSkills candidateSkills);
    void completeUpdated(int id);
    void savePersonal(PersonalInfoDto input);
    void saveCompanyInfo(CompanyInfoDto input);
    User getUser(int id);
    List<User> getCandidate();
    Page<User> getUsers(int id, int page, int size);
    Page<User> getNewCandidate(int id, int page, int size);
    Page<User> getUsersFavorites(int id, int page, int size);
    Page<User> getByCriteria(Criteria criteria, int page, int size);
}
