package com.myjob.jobseeker.repo;

import com.myjob.jobseeker.model.SearchHistory;
import org.springframework.data.domain.Page;

public interface SearchRepository {
    Page<SearchHistory> findPaginatedSearches(int id, int page, int size);
}
