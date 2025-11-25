package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.FavoriteModel;
import com.myjob.jobseeker.model.User;
import org.springframework.data.domain.Page;

public interface IFavoriteService {
    void updateFavorite(int idConnected, int id);
    Page<FavoriteModel> getPaginatedFavorites(int id, int page, int size);
    Page<User> getUsersFavorites(int id, int page, int size);
}
