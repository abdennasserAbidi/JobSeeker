package com.myjob.jobseeker.services;

import java.util.List;

import com.myjob.jobseeker.dtos.EducationDto;
import com.myjob.jobseeker.model.Education;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

private final UserRepository userRepository;

    public EducationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

      public void saveEducation(EducationDto input) {
        Education experience = new Education();
        experience.setId(input.getId());
        experience.setTitle(input.getTitle());
        experience.setSchoolName(input.getSchoolName());
        experience.setDateStart(input.getDateStart());
        experience.setDateEnd(input.getDateEnd());
        experience.setFieldStudy(input.getFieldStudy());
        experience.setPlace(input.getPlace());
        experience.setDegree(input.getDegree());
        experience.setDescription(input.getDescription());
        experience.setGrade(input.getGrade());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Education> list = user.getEducation();
        list.add(experience);
        user.setEducation(list);
        userRepository.save(user);
    }

    public List<Education> getAllEducations(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getEducation();
    }

}
