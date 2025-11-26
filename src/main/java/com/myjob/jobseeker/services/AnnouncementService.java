package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.IAnnouncementService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.post.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService implements IAnnouncementService {

    private final UserRepository userRepository;

    public AnnouncementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void makeAnnouncement(int id, AnnounceModel input) {
        User user = userRepository.findById(id).orElseThrow();
        List<AnnounceModel> list = user.getAnnounces();
        input.setIdCompany(user.getId());
        input.setCompanyName(user.getCompanyName());
        list.add(input);
        user.setAnnounces(list);
        userRepository.save(user);
    }

    @Override
    public void addComment(int idAnnounce, CommentsPost commentsPost) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                }
            }
        }

        if (indexPost != -1) {
            User user = userRepository.findById(idUser).orElseThrow();
            User userConnected = userRepository.findById(commentsPost.getIdCandidate()).orElseThrow();

            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                commentsPost.setUserName(userConnected.getFullName());
            } else {
                commentsPost.setUserName(userConnected.getCompanyName());
            }

            announceModel.getComments().add(commentsPost);

            user.getAnnounces().set(indexPost, announceModel);
            userRepository.save(user);
        }
    }

    @Override
    public void addLike(int idAnnounce, LikesPost likesPost) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                }
            }
        }

        if (indexPost != -1) {
            User user = userRepository.findById(idUser).orElseThrow();
            User userConnected = userRepository.findById(likesPost.getIdCandidate()).orElseThrow();

            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                likesPost.setUserName(userConnected.getFullName());
            } else {
                likesPost.setUserName(userConnected.getCompanyName());
            }

            announceModel.getLikes().add(likesPost);

            user.getAnnounces().set(indexPost, announceModel);
            userRepository.save(user);
        }
    }

    @Override
    public void removeLike(int idAnnounce, int idConnected) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                }
            }
        }

        if (indexPost != -1) {
            User user = userRepository.findById(idUser).orElseThrow();
            int indexToRemove = -1;
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            for (int k = 0; k < announceModel.getLikes().size(); k++) {
                LikesPost likesPost = announceModel.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected) {
                    indexToRemove = k;
                }
            }

            if (indexToRemove != -1) {
                announceModel.getLikes().remove(indexToRemove);
            }

            user.getAnnounces().set(indexPost, announceModel);
            userRepository.save(user);
        }
    }

    @Override
    public Page<AnnounceModel> getPaginatedAnnouncement(int id, int page, int size) {
        return userRepository.findPaginatedAnnouncement(id, page, size);
    }
}