package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.interfaces.IExperienceService;
import com.myjob.jobseeker.model.Experience;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class ExperienceService implements IExperienceService {

    private final UserRepository userRepository;

    public ExperienceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveExperience(ExperienceDto input) {
        boolean isPresent = false;
        int index = -1;

        Experience experience = new Experience();
        experience.setId(input.getId());
        experience.setTitle(input.getTitle());
        experience.setType(input.getType());
        experience.setTypeContract(input.getTypeContract());
        experience.setDateStart(input.getDateStart());
        experience.setDateEnd(input.getDateEnd());
        experience.setCompanyName(input.getCompanyName());
        experience.setFreelance(input.isFreelance());
        experience.setContract(input.isContract());
        experience.setCompanyName(input.getCompanyName());
        experience.setPlace(input.getPlace());
        experience.setSalary(input.getSalary());
        experience.setFreelanceFee(input.getFreelanceFee());
        experience.setAnotherActivitySector(input.getAnotherActivitySector());
        experience.setHourlyRate(input.getHourlyRate());
        experience.setNbDays(input.getNbDays());
        experience.setNbHours(input.getNbHours());
        experience.setFreelanceSalary(input.getFreelanceSalary());
        experience.setPerHourPaymentMethod(input.isPerHourPaymentMethod());
        experience.setPerDayPaymentMethod(input.isPerDayPaymentMethod());
        experience.setPerProjectPaymentMethod(input.isPerProjectPaymentMethod());
        experience.setCurrent(input.isCurrent());
        experience.setListSkills(input.getListSkills());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Experience> list = user.getExperiences();

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
            user.setExperiences(list);
        }
        userRepository.save(user);
    }

    @Override
    public List<Experience> getAllExperience(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getExperiences();
    }

    @Override
    public Page<Experience> getPaginatedExperiences(int id, int page, int size) {
        return userRepository.findPaginatedExperiences(id, page, size);
    }

    @Override
    public void removeExperience(int idUser, int idExperience) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Experience> list = user.getExperiences();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idExperience) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setExperiences(list);
        }
        userRepository.save(user);
    }
}