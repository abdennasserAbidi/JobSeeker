package com.myjob.jobseeker.repo.messenger;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<ChatModel, Integer>, MessageRepository {
    @Query("""
    {
       $or: [
         { $and: [ { 'userConnectedId': ?0 }, { 'userReceivedId': ?1 } ] },
         { $and: [ { 'userConnectedId': ?1 }, { 'userReceivedId': ?0 } ] }
       ]
    }
    """)
    List<ChatModel> findConversation(int user1, int user2);

    // Fetch messages where the user is either sender or receiver, with pagination
    @Query("""
        {
          $or: [
            { "userConnectedId": ?0 },
            { "userReceivedId": ?0 }
          ]
        }
    """)
    Page<ChatModel> findAllMessagesForUser(int userId, Pageable pageable);
}
