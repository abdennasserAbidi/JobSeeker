package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;

import com.myjob.jobseeker.model.InvitationModel;

public interface InvitationRepository {
    Page<InvitationModel> findPaginatedInvitations(int id, int page, int size);
}
