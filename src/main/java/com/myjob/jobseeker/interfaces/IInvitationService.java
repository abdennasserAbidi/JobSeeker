package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.FilterInvitationBody;
import com.myjob.jobseeker.model.InvitationModel;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IInvitationService {
    Page<InvitationModel> getPaginatedInvitations(int id, int page, int size);
    Page<InvitationModel> getPaginatedInvitationsFiltered(FilterInvitationBody request, int page, int size);
    Page<InvitationModel> getPaginatedInvitationsByContract(int id, int page, int size);
    InvitationUser getInvitationDetail(int id, int idInvitation);
    void sendInvitation(int id, InvitationModel input);
    void finishProcess(int id, InvitationModel input);
    String acceptRejectInvitation(InvitationDto invitationDto);
    void deleteInvitation(int idInvitation, int idFrom);
    void deleteInvitationFromAll(int idInvitation);
    java.util.List<InvitationModel> getAllInvitations(int idCompany);

    List<String> getCompaniesValidated();
    List<String> getInstitutesValidated();
}