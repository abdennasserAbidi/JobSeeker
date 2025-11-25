package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.IFavoriteService;
import com.myjob.jobseeker.model.FavoriteModel;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService implements IFavoriteService {

    private final UserRepository userRepository;

    public FavoriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void updateFavorite(int idConnected, int id) {
        User user = userRepository.findById(idConnected).orElseThrow();

        List<FavoriteModel> l = user.getFavorites();
        FavoriteModel f = new FavoriteModel();

        User candidate = userRepository.findById(id).orElseThrow();
        candidate.setFavorite(true);

        f.setId(candidate.getId());
        f.setEmail(candidate.getEmail());
        f.setName(candidate.getFullName());
        f.setPhone(candidate.getPhone());

        l.add(f);
        user.setFavorites(l);

        userRepository.save(user);
    }

    @Override
    public Page<FavoriteModel> getPaginatedFavorites(int id, int page, int size) {
        return userRepository.findPaginatedFavorites(id, page, size);
    }

    @Override
    public Page<User> getUsersFavorites(int id, int page, int size) {
        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new java.util.ArrayList<>();

        if (!user.getFavorites().isEmpty()) {
            for (FavoriteModel i : user.getFavorites()) {
                User favoriteUser = userRepository.findById(i.getId()).orElseThrow();
                newUsers.add(favoriteUser);
            }
        }

        org.springframework.data.domain.PageRequest pageable = org.springframework.data.domain.PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUsers.size());

        return new org.springframework.data.domain.PageImpl<>(newUsers.subList(start, end), pageable, newUsers.size());
    }
}