package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvitationDto {

    private int idConnected;
    private InvitationModel invitationModel;

}
