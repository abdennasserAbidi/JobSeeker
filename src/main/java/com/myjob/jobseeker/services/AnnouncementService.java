package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.IAnnouncementService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.post.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    @Override
    public Boolean getLiked(int idAnnounce, int idConnected) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexUser = -1;
        int indexPost = -1;

        int count = 0;
        Boolean isThere = false;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                    indexUser = i;
                }
            }
        }


        if (indexPost != -1) {

            User user = userRepository.findById(idUser).orElseThrow();
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            for (int k = 0; k < announceModel.getLikes().size(); k++) {
                LikesPost likesPost = announceModel.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected) {
                    count += 1;
                    isThere = true;
                }
            }
        }

        return isThere;
    }

    @Override
    public List<Boolean> getLikedAllPost(int idConnected) {
        List<User> users = userRepository.findAll();
        List<Boolean> booleanList = new ArrayList<>();

        for (User value : users) {
            List<AnnounceModel> announceModelList = value.getAnnounces();
            for (AnnounceModel model : announceModelList) {
                boolean isThere = false;
                for (int k = 0; k < model.getLikes().size(); k++) {
                    LikesPost likesPost = model.getLikes().get(k);
                    isThere = likesPost.getIdCandidate() == idConnected;
                }
                booleanList.add(isThere);
            }
        }
        return booleanList;
    }

    @Override
    public List<Boolean> getLikedAllPostCompany(int idConnected) {
        List<User> users = userRepository.findAll();
        List<Boolean> booleanList = new ArrayList<>();

        for (User value : users) {
            List<AnnounceModel> announceModelList = value.getAnnounces();
            for (AnnounceModel model : announceModelList) {
                if (model.getIdCompany() == idConnected) {
                    boolean isThere = false;
                    for (int k = 0; k < model.getLikes().size(); k++) {
                        LikesPost likesPost = model.getLikes().get(k);
                        isThere = likesPost.getIdCandidate() == idConnected;
                    }
                    booleanList.add(isThere);
                }
            }
        }
        return booleanList;
    }

    @Override
    public List<Integer> getNumberLikeAllPostsCompany(int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<Integer> booleanList = new ArrayList<>();

        List<AnnounceModel> announceModelList = user.getAnnounces();
        for (AnnounceModel model : announceModelList) {
            int isThere = 0;
            for (int k = 0; k < model.getLikes().size(); k++) {
                LikesPost likesPost = model.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected)
                    isThere += 1;
            }
            booleanList.add(isThere);
        }
        return booleanList;
    }

    @Override
    public List<Integer> getNumberCommentAllPostsCompany(int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<Integer> booleanList = new ArrayList<>();

        List<AnnounceModel> announceModelList = user.getAnnounces();
        for (AnnounceModel model : announceModelList) {
            int isThere = 0;
            for (int k = 0; k < model.getComments().size(); k++) {
                CommentsPost likesPost = model.getComments().get(k);
                if (likesPost.getIdCandidate() == idConnected)
                    isThere += 1;
            }
            booleanList.add(isThere);
        }
        return booleanList;
    }

    @Override
    public List<CommentsPost> getCommentAllPostsCompany(int idAnnounce, int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<CommentsPost> commentList = new ArrayList<>();


        List<AnnounceModel> announceModelList = user.getAnnounces().stream().filter(announceModel ->
                announceModel.getIdAnnounce() == idAnnounce
        ).toList();
        for (AnnounceModel model : announceModelList) {
            commentList.addAll(model.getComments());
        }
        return commentList;
    }

    @Override
    public List<Integer> getNumberLikeAllPosts(int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<Integer> booleanList = new ArrayList<>();

        List<AnnounceModel> announceModelList = user.getAnnounces();
        for (AnnounceModel model : announceModelList) {
            int isThere = 0;
            for (int k = 0; k < model.getLikes().size(); k++) {
                LikesPost likesPost = model.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected)
                    isThere += 1;
            }
            booleanList.add(isThere);
        }
        return booleanList;
    }

    @Override
    public List<Integer> getNumberCommentAllPosts(int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<Integer> booleanList = new ArrayList<>();

        List<AnnounceModel> announceModelList = user.getAnnounces();
        for (AnnounceModel model : announceModelList) {
            int isThere = 0;
            for (int k = 0; k < model.getComments().size(); k++) {
                CommentsPost likesPost = model.getComments().get(k);
                if (likesPost.getIdCandidate() == idConnected)
                    isThere += 1;
            }
            booleanList.add(isThere);
        }
        return booleanList;
    }
}