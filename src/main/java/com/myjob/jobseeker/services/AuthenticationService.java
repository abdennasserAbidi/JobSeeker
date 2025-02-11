package com.myjob.jobseeker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.myjob.jobseeker.dtos.EducationDto;
import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.dtos.LoginUserDto;
import com.myjob.jobseeker.dtos.PersonalInfoDto;
import com.myjob.jobseeker.dtos.RegisterUserDto;
import com.myjob.jobseeker.dtos.UserResponse;
import com.myjob.jobseeker.model.Education;
import com.myjob.jobseeker.model.Experience;
import com.myjob.jobseeker.model.FavoriteModel;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    @Autowired
	JavaMailSender javaMailSender;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateFavorite(int idConnected, int id) {
        User user = userRepository.findById(idConnected).orElseThrow();
        
        List<FavoriteModel> l = user.getFavorites();
        FavoriteModel f = new FavoriteModel();
    
        User candidate = userRepository.findById(id).orElseThrow();

        f.setId(candidate.getId());
        f.setEmail(candidate.getEmail());
        f.setName(candidate.getFullName());
        f.setPhone(candidate.getPhone());

        l.add(f);
        user.setFavorites(l);

        userRepository.save(user);
    }


    public Page<FavoriteModel> getPaginatedFavorites(int id, int page, int size) {
        return userRepository.findPaginatedFavorites(id, page, size);
    }

    public void savePersonal(PersonalInfoDto input) {
        User user = userRepository.findById(input.getId()).orElseThrow();
        
        user.setFullName(input.getFullName());
        user.setNationality(input.getNationality());
        user.setActivitySector(input.getActivitySector());
        user.setAddress(input.getAddress());
        user.setBirthDate(input.getBirthDate());
        user.setSexe(input.getSexe());
        user.setSituation(input.getSituation());
        user.setRangeSalary(input.getRangeSalary());
        user.setAvailability(input.getAvailability());
        user.setPreferredActivitySector(input.getPreferredActivitySector());
        user.setPhone(input.getPhone());

        userRepository.save(user);
    }

    public User getUser(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user;
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

        for(int i = 0; i < list.size(); i++) {
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

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public List<Education> getAllEducations(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getEducation();
    }

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
        experience.setPlace(input.getPlace());
        experience.setSalary(input.getSalary());
        experience.setFreelanceFee(input.getFreelanceFee());
        experience.setAnotherActivitySector(input.getAnotherActivitySector());
        experience.setHourlyRate(input.getHourlyRate());
        experience.setNbDays(input.getNbDays());
        experience.setNbHours(input.getNbHours());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Experience> list = user.getExperiences();

        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == input.getId()) {
                isPresent = true;
                index = i;
            }
        }

        System.err.println("isPresent   "+input.getId());

        if (isPresent) {
            list.set(index, experience);
        } else {
            list.add(experience);
            user.setExperiences(list);
        }
        userRepository.save(user);
    }

    public Page<User> getUsers(int page, int size) {

        return userRepository.findAll(PageRequest.of(page - 1, size));
    }


    public Page<Experience> getPaginatedExperiences(int id, int page, int size) {
        return userRepository.findPaginatedExperiences(id, page, size);
    }

    public Page<Education> getPaginatedEducations(int id, int page, int size) {
        return userRepository.findPaginatedEducations(id, page, size);
    }

    public List<Experience> getAllExperience(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getExperiences();
    }

    public void removeExperience(int idUser, int idExperience) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Experience> list = user.getExperiences();

        boolean isPresent = false;
        int index = -1;

        for(int i = 0; i < list.size(); i++) {
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


    public void removeEducation(int idUser, int idEducation) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Education> list = user.getEducation();

        boolean isPresent = false;
        int index = -1;

        for(int i = 0; i < list.size(); i++) {
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

    public UserResponse signup(RegisterUserDto input) {
        User user = new User();
        user.setId(input.getId());
        user.setEmail(input.getEmail());
        user.setFullName(input.getFullName());
        user.setCompanyName(input.getCompanyName());
        user.setRole(input.getRole());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        boolean existingUser = userRepository.findByEmail(input.getEmail()).isPresent();

        UserResponse userResponse = new UserResponse();

        if (existingUser) {
            userResponse.setMessage("User already exist");
        } else userResponse.setUser(userRepository.save(user));
        
        return userResponse;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

	public User authenticateByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

	public boolean hasExipred(LocalDateTime expiryDateTime) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return expiryDateTime.isAfter(currentDateTime);
	}

}
