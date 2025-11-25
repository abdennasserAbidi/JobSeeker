package com.myjob.jobseeker.services;

import java.util.List;

import com.myjob.jobseeker.dtos.EducationDto;
import com.myjob.jobseeker.model.Education;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

private final UserRepository userRepository;

    public EducationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<Education> getPaginatedEducations(int id, int page, int size) {
        return userRepository.findPaginatedEducations(id, page, size);
    }
    public List<Education> getAllEducations(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getEducation();
    }
    public void saveEducation(EducationDto input) {


        boolean isPresent = false;
        int index = -1;

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
        experience.setStillStudying(input.isStillStudying());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Education> list = user.getEducation();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == input.getId()) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.set(index, experience);
        } else {
            list.add(experience);
            user.setEducation(list);
        }
        userRepository.save(user);
    }


    public void removeEducation(int idUser, int idEducation) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Education> list = user.getEducation();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idEducation) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setEducation(list);
        }
        userRepository.save(user);
    }

}
