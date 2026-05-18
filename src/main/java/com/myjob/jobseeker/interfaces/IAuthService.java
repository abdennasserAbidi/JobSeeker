package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.CandidateSkills;
import com.myjob.jobseeker.model.CategoryModel;
import com.myjob.jobseeker.model.ProfessionalStatus;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.ValidationStatus;

import org.springframework.data.domain.Page;

public interface IAuthService {
    NotificationMessage verifyAccountCompany(int id, String status);
    NotificationMessage verifyAccountCandidate(int id, String status);
    String updateToken(int email, String token);
    UserResponse signup(RegisterUserDto input);
    UserResponse authenticate(LoginUserDto input);
    User authenticateByEmail(String email);
    String createPasswordResetTokenForUser(String userEmail);
    boolean validatePasswordResetToken(String token);
    void updatePassword(String token, String newPassword);
    void saveCandidateStatus(ValidationStatus validationStatus);
    ValidationStatus getStatusCandidateValidation(int id);
    java.util.List<ValidationStatus> getListStatusCandidateValidation(int id);
    void saveServiceInfo(ServiceInfoDto input);

    Page<User> getByCriteria(Criteria criteria, int page, int size);
    void countDownTrial(int idUser);

    Page<User> getNewCandidate(int id, int page, int size);

    Page<User> getUsers(int id, int page, int size);
    Page<User> getUserService(int id, int page, int size);
    Page<User> getUserServiceFiltered(String word, int page, int size);

    UserResponse getUser(int id);

    void saveCompanyInfo(CompanyInfoDto input);

    void completeUpdated(int id);

    void saveSkillsInfo(CandidateSkills candidateSkills);

    void saveProfessionalInfo(ProfessionalStatus professionalStatus);

    void savePersonal(PersonalInfoDto input);

    Page<User> getUsersFavorites(int id, int page, int size);
    Page<User> getUserServiceFilteredList(CategoryModel criteria, int page, int size);
}
