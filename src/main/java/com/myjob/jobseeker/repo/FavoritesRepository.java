package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;

import com.myjob.jobseeker.model.FavoriteModel;

public interface FavoritesRepository {
    Page<FavoriteModel> findPaginatedFavorites(int id, int page, int size);

}
