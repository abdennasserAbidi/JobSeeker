package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.interfaces.IInvitationService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationService implements IInvitationService {

    private final UserRepository userRepository;

    public InvitationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<InvitationModel> getPaginatedInvitations(int id, int page, int size) {
        return userRepository.findPaginatedInvitations(id, page, size);
    }

    @Override
    public Page<InvitationModel> getPaginatedInvitationsFiltered(FilterInvitationBody request, int page, int size) {
        return userRepository.findPaginatedInvitationsByTag(request, page, size);
    }

    @Override
    public Page<InvitationModel> getPaginatedInvitationsByContract(int id, int page, int size) {
        return userRepository.findPaginatedInvitationsByContract(id, page, size);
    }

    @Override
    public InvitationUser getInvitationDetail(int id, int idInvitation) {
        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> invitations = user.getInvitations();

        InvitationModel invitationModel = null;

        for (InvitationModel invi : invitations) {
            if (invi.getIdInvitation() == idInvitation) {
                invitationModel = invi;
                break;
            }
        }

        InvitationUser invitationUser = new InvitationUser();
        invitationUser.setInvitationModel(invitationModel);

        if (invitationModel != null) {
            User candidate = userRepository.findById(invitationModel.getIdTo()).orElseThrow();
            invitationUser.setUserCandidate(candidate);

            User company = userRepository.findById(invitationModel.getIdCompany()).orElseThrow();
            invitationUser.setUserCompany(company);
        }

        return invitationUser;
    }

    @Override
    public void sendInvitation(int id, InvitationModel input) {
        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> list = user.getInvitations();
        List<InvitationModel> filteredList = list.stream().filter(invitation -> invitation.getIdTo() == input.getIdTo()).toList();

        if (filteredList.isEmpty()) {
            InvitationModel experience = new InvitationModel();
            experience.setIdInvitation(input.getIdInvitation());
            experience.setIdTo(input.getIdTo());
            experience.setIdCompany(input.getIdCompany());
            experience.setCompanyName(input.getCompanyName());
            experience.setMessage(input.getMessage());
            experience.setStatus(input.getStatus());
            experience.setDescription(input.getDescription());
            experience.setTypeContract(input.getTypeContract());
            experience.setDescriptionContract(input.getDescriptionContract());
            experience.setDate(input.getDate());
            experience.setFullName(input.getFullName());
            experience.setGender(input.getGender());
            experience.setNameContract(input.getNameContract());
            experience.setDuration(input.getDuration());

            saveCompanyInvitation(id, experience);
            saveUserInvitation(input.getIdTo(), experience);
        }
    }

    @Override
    public void finishProcess(int id, InvitationModel input) {
        User user = userRepository.findById(id).orElseThrow();
        updateInvitation(user, input);

        User candidate = userRepository.findById(input.getIdTo()).orElseThrow();
        updateInvitation(candidate, input);
    }

    @Override
    public String acceptRejectInvitation(InvitationDto invitationDto) {
        InvitationModel input = invitationDto.getInvitationModel();

        int idCandidate = input.getIdTo();
        updateStatus(idCandidate, input);

        int idCompany = invitationDto.getIdConnected();
        updateStatus(idCompany, input);

        return input.getStatus();
    }

    @Override
    public void deleteInvitation(int idInvitation, int idFrom) {
        User user = userRepository.findById(idFrom).orElseThrow();

        List<InvitationModel> newListInvitation = new ArrayList<>();

        List<InvitationModel> listInvitations = user.getInvitations();

        for (InvitationModel invitationModel : listInvitations) {
            if (invitationModel.getIdInvitation() != idInvitation) {
                newListInvitation.add(invitationModel);
            }
        }

        user.setInvitations(newListInvitation);
        userRepository.save(user);
    }

    @Override
    public void deleteInvitationFromAll(int idInvitation) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<InvitationModel> newListInvitation = new ArrayList<>();

            List<InvitationModel> listInvitations = user.getInvitations();

            for (InvitationModel invitationModel : listInvitations) {
                if (invitationModel.getIdInvitation() != idInvitation) {
                    newListInvitation.add(invitationModel);
                }
            }

            user.setInvitations(newListInvitation);
        }

        userRepository.saveAll(userList);
    }

    @Override
    public List<InvitationModel> getAllInvitations(int idCompany) {
        User user = userRepository.findById(idCompany).orElseThrow();
        List<InvitationModel> invitations = user.getInvitations();
        return invitations;
    }

    @Override
    public List<String> getCompaniesValidated() {
        List<User> users = userRepository.findAll();
        List<String> companyNames = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals("Company") || user.getRole().equals("Entreprise")) {
                /*if (user.isVerified()) {
                    companyNames.add(user.getCompanyName());
                }*/
                companyNames.add(user.getCompanyName());
            }
        }
        return companyNames;
    }
    // private / helper methods (copied) used internally
    private void saveCompanyInvitation(int id, InvitationModel input) {
        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> list = user.getInvitations();
        list.add(input);
        user.setInvitations(list);
        userRepository.save(user);
    }

    private void saveUserInvitation(int idUser, InvitationModel input) {
        boolean isPresent = false;
        int index = -1;

        User user = userRepository.findById(idUser).orElseThrow();
        List<InvitationModel> list = user.getInvitations();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdInvitation() == input.getIdInvitation()) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.set(index, input);
        } else {
            list.add(input);
            user.setInvitations(list);
        }

        userRepository.save(user);
    }

    private void updateStatus(int idCandidate, InvitationModel input) {
        User candidate = userRepository.findById(idCandidate).orElseThrow();
        List<InvitationModel> list = candidate.getInvitations();

        InvitationModel i = null;
        int index = -1;
        for (int j = 0; j < list.size(); j++) {
            InvitationModel invitationModel = list.get(j);
            if (invitationModel.getIdInvitation() == input.getIdInvitation()) {
                index = j;
                i = invitationModel;
            }
        }

        if (i != null) {
            i.setIdInvitation(input.getIdInvitation());
            i.setIdTo(input.getIdTo());
            i.setStatus(input.getStatus());
            i.setAccepted(input.isAccepted());
            i.setIdCompany(input.getIdCompany());
            i.setCompanyName(input.getCompanyName());
            i.setMessage(input.getMessage());
            i.setDescription(input.getDescription());
            i.setTypeContract(input.getTypeContract());
            i.setDate(input.getDate());
            i.setFullName(input.getFullName());
            i.setGender(input.getGender());

            list.set(index, i);
            candidate.setInvitations(list);

            userRepository.save(candidate);
        }
    }

    private void updateInvitation(User user, InvitationModel input) {
        List<InvitationModel> list = user.getInvitations();
        InvitationModel selectedInvitation = new InvitationModel();
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            InvitationModel invitationModel = list.get(i);
            if (invitationModel.getIdInvitation() == input.getIdInvitation()) {
                index = i;
                selectedInvitation = invitationModel;
            }
        }

        if (index != -1) {
            selectedInvitation.setStatus(input.getStatus());
            selectedInvitation.setReason(input.getReason());
            selectedInvitation.setDateEnd(input.getDateEnd());

            list.set(index, selectedInvitation);

            user.setInvitations(list);

            userRepository.save(user);
        }
    }
}