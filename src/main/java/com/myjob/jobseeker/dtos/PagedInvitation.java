package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.InvitationModel;
import lombok.Data;

import java.util.List;

@Data
public class PagedInvitation {

    List<InvitationModel> content;
    int page;
    int size;
    long totalElements;
    int totalPages;

}
