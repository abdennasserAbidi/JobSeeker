package com.myjob.jobseeker.interfaces;


import com.myjob.jobseeker.model.SearchHistory;
import com.myjob.jobseeker.model.User;
import org.springframework.data.domain.Page;

public interface ISearchService {
    void saveSearchHistory(int idUserConnected, SearchHistory searchHistory);
    Page<SearchHistory> getPaginatedSearches(int id, int page, int size);
    Page<SearchHistory> searchCandidate(String word, int id, int page, int size);
    void removeSearchHistory(int idUserConnected, int idUserToDelete);

    Page<User> getUserFiltered(String word, int id, int page, int size);
}
