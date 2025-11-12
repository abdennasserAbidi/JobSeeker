package com.myjob.jobseeker.repo;

import com.myjob.jobseeker.model.FilterInvitationBody;
import com.myjob.jobseeker.model.InvitationModel;
import org.springframework.data.domain.Page;

public interface InvitationRepository {
    Page<InvitationModel> findPaginatedInvitations(int id, int page, int size);

    Page<InvitationModel> findPaginatedInvitationsByContract(int id, int page, int size);

    Page<InvitationModel> findPaginatedInvitationsByTag(FilterInvitationBody request, int page, int size);
}
