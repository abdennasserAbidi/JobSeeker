package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.ISearchService;
import com.myjob.jobseeker.model.InvitationModel;
import com.myjob.jobseeker.model.SearchHistory;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
public class SearchService implements ISearchService {

    private final UserRepository userRepository;

    public SearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveSearchHistory(int idUserConnected, SearchHistory searchHistory) {
        boolean isPresent = false;

        User user = userRepository.findById(idUserConnected).orElseThrow();
        List<SearchHistory> list = user.getSearchHistories();

        for (SearchHistory search : list) {
            if (search.getIdUser() == searchHistory.getIdUser()) {
                isPresent = true;
                break;
            }
        }

        if (!isPresent) {
            list.add(searchHistory);
            user.setSearchHistories(list);
        }

        userRepository.save(user);
    }

    @Override
    public Page<SearchHistory> getPaginatedSearches(int id, int page, int size) {
        return userRepository.findPaginatedSearches(id, page, size);
    }

    @Override
    public Page<SearchHistory> searchCandidate(String word, int id, int page, int size) {
        List<User> candidates = userRepository.findAll();

        List<User> newUsers = new ArrayList<>();
        for (User c : candidates) {
            if (c.getRole().equals("Candidate") && c.getFullName().toLowerCase().contains(word)) {
                newUsers.add(c);
            }
        }

        List<User> combined;

        if (word.isEmpty()) combined = candidates;
        else combined = newUsers;

        List<SearchHistory> list = new ArrayList<>();
        for (User c : combined) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setIdUser(c.getId());
            searchHistory.setGender(c.getSexe());
            searchHistory.setFullName(c.getFullName());

            List<com.myjob.jobseeker.model.Experience> exp = c.getExperiences();
            if (!exp.isEmpty()) {
                String date = exp.get(0).getDateStart();
                if (date.contains(",")) {
                    String[] dates = date.split(", ");
                    if (dates.length > 0) {
                        int year = Integer.parseInt(dates[2]);
                        int currentYear = Year.now().getValue();

                        int diff = currentYear - year;
                        if (diff > 0) {
                            searchHistory.setExperience(diff + " years experiences");
                        }
                    }
                }

            }

            list.add(searchHistory);
        }

        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUsers.size());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override
    public void removeSearchHistory(int idUserConnected, int idUserToDelete) {
        User user = userRepository.findById(idUserConnected).orElseThrow();
        List<SearchHistory> list = user.getSearchHistories();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUser() == idUserToDelete) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setSearchHistories(list);
        }
        userRepository.save(user);
    }
    @Override
    public Page<User> getUserFiltered(String word, int id, int page, int size) {

        Optional<User> optionalUser = userRepository.findById(id);
        List<User> newUsers = new ArrayList<>();

        AtomicReference<Page<User>> userPage = new AtomicReference<>();
        userPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        optionalUser.ifPresent(user -> {
            List<User> allUsers = userRepository.findAll();

            for (User candidate : allUsers) {

                boolean nameContains;
                if (candidate.isCandidate())
                    nameContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(candidate.getFullName()).find();
                else
                    nameContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(candidate.getCompanyName()).find();


                if (candidate.getId() != id && !candidate.isFirstTimeUse() && nameContains) {
                    newUsers.add(candidate);
                }
            }

            if (newUsers.isEmpty()) {
                userPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));
            } else {
                int s = Math.min(size, newUsers.size());

                PageRequest pageable = PageRequest.of(page - 1, s);
                final int start = (int) pageable.getOffset();
                final int end = Math.min((start + pageable.getPageSize()), s);
                userPage.set(new PageImpl<>(newUsers.subList(start, end), pageable, s));
            }
        });

        return userPage.get();
    }

}