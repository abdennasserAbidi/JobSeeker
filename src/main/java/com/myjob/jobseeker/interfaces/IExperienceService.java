package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.model.Experience;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExperienceService {
    void saveExperience(ExperienceDto input);
    List<Experience> getAllExperience(int id);
    Page<Experience> getPaginatedExperiences(int id, int page, int size);
    void removeExperience(int idUser, int idExperience);
}
