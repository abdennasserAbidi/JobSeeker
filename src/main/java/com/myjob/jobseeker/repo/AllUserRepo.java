package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.myjob.jobseeker.model.User;

public interface AllUserRepo {
    Page<User> findPaginatedAllUser(PageRequest pageable);
}
