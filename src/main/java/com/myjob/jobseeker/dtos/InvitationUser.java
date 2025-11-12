package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.InvitationModel;
import com.myjob.jobseeker.model.User;
import lombok.Data;

@Data
public class InvitationUser {
    private InvitationModel invitationModel;
    private User user;
}
