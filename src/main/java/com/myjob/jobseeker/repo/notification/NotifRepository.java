package com.myjob.jobseeker.repo.notification;

import com.myjob.jobseeker.model.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifRepository  extends MongoRepository<NotificationModel, Integer>, NotificationRepository {
}
