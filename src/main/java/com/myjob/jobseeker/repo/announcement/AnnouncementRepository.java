package com.myjob.jobseeker.repo.announcement;

import com.myjob.jobseeker.model.announces.AnnounceModel;
import org.springframework.data.domain.Page;

public interface AnnouncementRepository {
    Page<AnnounceModel> findPaginatedAnnouncement(int id, int page, int size);
}
