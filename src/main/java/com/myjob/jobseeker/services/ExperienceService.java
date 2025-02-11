package com.myjob.jobseeker.services;

import org.springframework.stereotype.Service;
import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.model.Experience;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;

import java.util.List;

@Service
public class ExperienceService {

    private final UserRepository userRepository;

    public ExperienceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

      public void saveExperience(ExperienceDto input) {
        Experience experience = new Experience();
        experience.setId(input.getId());
        experience.setTitle(input.getTitle());
        experience.setType(input.getType());
        experience.setDateStart(input.getDateStart());
        experience.setDateEnd(input.getDateEnd());
        experience.setCompanyName(input.getCompanyName());
        experience.setPlace(input.getPlace());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Experience> list = user.getExperiences();
        list.add(experience);
        user.setExperiences(list);
        userRepository.save(user);
    }

    public List<Experience> getAllExperience(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getExperiences();
    }

}
