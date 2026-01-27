package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IAnnouncementService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.announces.AnnounceModel;
import com.myjob.jobseeker.model.announces.AnnounceResponse;
import com.myjob.jobseeker.model.post.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void updateAnnouncement(int id, AnnounceModel input) {
        Optional<User> u = userRepository.findById(id);
        u.ifPresent(user -> {
            input.setIdCompany(user.getId());
            input.setCompanyName(user.getCompanyName());

            List<AnnounceModel> list = user.getAnnounces();

            int indicator = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getIdAnnounce() == input.getIdAnnounce())
                    indicator = i;
            }

            if (indicator != -1) {
                list.set(indicator, input);
                user.setAnnounces(list);
                userRepository.save(user);
            }
        });
    }

    @Override
    public AnnounceResponse findAnnounceCompany(String type, int idConnected) {
        Optional<User> userValue = userRepository.findById(idConnected);
        AnnounceResponse announceResponse = new AnnounceResponse();
        announceResponse.setIdAnnounceResponse(5211);
        String confirm = "";
        if (userValue.isPresent()) {
            User user = userValue.get();

            List<AnnounceModel> announceModelList = user.getAnnounces().stream().filter(announceModel ->
                    announceModel.getPostType().equals(type)
            ).toList();

            if (announceModelList.isEmpty()) {
                confirm = "Annonce n'existe pas";
            } else {
                announceResponse.setAnnounceModel(announceModelList);
            }
        } else confirm = "Utilisateur non trouvé";

        announceResponse.setMessage(confirm);

        System.out.println(announceResponse.getAnnounceModel());
        return announceResponse;
    }

    @Override
    public Page<AnnounceModel> findAnnounceCandidate(String type, int page, int size) {
        List<User> users = userRepository.findAll();
        List<AnnounceModel> list = new ArrayList<>();

        for (User user : users) {
            if (user.getRole().equals("Company") || user.getRole().equals("Entreprise")) {
                List<AnnounceModel> announceModelList = user.getAnnounces().stream().filter(announceModel ->
                        announceModel.getPostType().equals(type)
                ).toList();

                list.addAll(announceModelList);
            }
        }

        PageRequest pageable = PageRequest.of(page - 1, size);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());

        Page<AnnounceModel> pager;

        if (start < list.size() && start < end) {
            pager = new PageImpl<>(list.subList(start, end), pageable, list.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, list.size());

        return pager;
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
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);
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
        for (User user : users) {
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
        for (User user : users) {
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
        //return userRepository.findPaginatedAnnouncement(id, page, size);
        PageRequest pageable = PageRequest.of(page - 1, size);
        final int start = (int) pageable.getOffset();

        Page<AnnounceModel> pager;

        List<User> userList = userRepository.findAll();
        List<AnnounceModel> newList = new ArrayList<>();
        for (User user : userList) {
            if (!user.isCandidate()) {
                List<AnnounceModel> announceModelList = user.getAnnounces();
                newList.addAll(announceModelList);
            }
        }

        final int end = Math.min((start + pageable.getPageSize()), newList.size());

        if (start < newList.size() && start < end) {
            pager = new PageImpl<>(newList.subList(start, end), pageable, newList.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newList.size());

        return pager;
    }

    @Override
    public AnnounceModel getAnnouncement(int idAnnounce, int idCompany) {
        User company = userRepository.findById(idCompany).orElseThrow();
        List<AnnounceModel> list = company.getAnnounces().stream().filter(post -> post.getIdAnnounce() == idAnnounce).toList();
        if (list.isEmpty()) return new AnnounceModel();
        else return list.get(0);
    }

    @Override
    public Page<AnnounceModel> getPaginatedAnnouncementCandidate(int page, int size) {
        List<User> userList = userRepository.findAll();
        List<AnnounceModel> newList = new ArrayList<>();
        for (User user : userList) {
            List<AnnounceModel> announceModelList = user.getAnnounces();
            newList.addAll(announceModelList);
        }

        PageRequest pageable = PageRequest.of(page - 1, size);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newList.size());

        Page<AnnounceModel> pager;

        if (start < newList.size() && start < end) {
            pager = new PageImpl<>(newList.subList(start, end), pageable, newList.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newList.size());

        return pager;
    }

    @Override
    public Boolean getLiked(int idAnnounce, int idConnected) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexPost = -1;

        boolean isThere = false;

        for (User user : users) {
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
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            for (int k = 0; k < announceModel.getLikes().size(); k++) {
                LikesPost likesPost = announceModel.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected) {
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
                int k = 0;
                while (k < model.getLikes().size() && !isThere) {
                    LikesPost likesPost = model.getLikes().get(k);
                    if (likesPost.getIdCandidate() != idConnected) {
                        k += 1;
                    } else isThere = true;
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
                    int k = 0;
                    while (k < model.getLikes().size() && !isThere) {
                        LikesPost likesPost = model.getLikes().get(k);
                        if (likesPost.getIdCandidate() != idConnected) {
                            k += 1;
                        } else isThere = true;
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
            booleanList.add(model.getLikes().size());
        }
        return booleanList;
    }

    @Override
    public List<Integer> getNumberCommentAllPostsCompany(int idConnected) {
        User user = userRepository.findById(idConnected).orElseThrow();
        List<Integer> booleanList = new ArrayList<>();

        List<AnnounceModel> announceModelList = user.getAnnounces();
        for (AnnounceModel model : announceModelList) {
            booleanList.add(model.getComments().size());
        }
        return booleanList;
    }

    @Override
    public List<Integer> getNumberLikeAllPosts() {
        List<User> users = userRepository.findAll();
        List<Integer> booleanList = new ArrayList<>();

        for (User user : users) {
            if (user.getRole().equals("Company") || user.getRole().equals("Entreprise")) {
                List<AnnounceModel> announceModelList = user.getAnnounces();
                for (AnnounceModel model : announceModelList) {
                    booleanList.add(model.getLikes().size());
                }
            }
        }

        return booleanList;
    }

    @Override
    public List<Integer> getNumberCommentAllPosts() {
        List<User> users = userRepository.findAll();
        List<Integer> booleanList = new ArrayList<>();

        for (User user : users) {
            if (user.getRole().equals("Company") || user.getRole().equals("Entreprise")) {
                List<AnnounceModel> announceModelList = user.getAnnounces();
                for (AnnounceModel model : announceModelList) {
                    booleanList.add(model.getComments().size());
                }
            }
        }
        return booleanList;
    }

    @Override
    public List<CommentsPost> getCommentAllPostsCompany(int idAnnounce) {
        List<AnnounceModel> announceList = userRepository.getComments(idAnnounce);
        List<CommentsPost> commentList = new ArrayList<>();

        if (!announceList.isEmpty()) {
            for (AnnounceModel model : announceList) {
                commentList.addAll(model.getComments());
            }
        }

        return commentList;
    }

    @Override
    public ExperienceResponse deletePostCompany(int idAnnounce, int idConnected) {
        Optional<User> userValue = userRepository.findById(idConnected);
        String confirm = "";
        if (userValue.isPresent()) {
            User user = userValue.get();
            List<AnnounceModel> announceModelList = user.getAnnounces().stream().filter(announceModel ->
                    announceModel.getIdAnnounce() == idAnnounce
            ).toList();

            if (announceModelList.isEmpty()) {
                confirm = "Annonce n'existe pas";
            } else {
                List<AnnounceModel> list = user.getAnnounces();
                list.remove(announceModelList.get(0));
                user.setAnnounces(list);

                userRepository.save(user);
                confirm = "deleted successfully";
            }
        } else confirm = "Utilisateur non trouvé";

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(5211);
        experienceResponse.setMessage(confirm);

        return experienceResponse;
    }
}