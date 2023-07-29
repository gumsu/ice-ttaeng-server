package com.example.teamtwelvebackend.activity.speedgame.repository;

import com.example.teamtwelvebackend.activity.speedgame.domain.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByRoomNameAndQuestionIdAndAnswerIdIn(String roomName, Long questionId, List<Long> answerId);
//    Optional<UserAnswer> findByRoomNameAndNumber(String roomName, int number);
    Integer countByRoomNameAndQuestionId(String roomName, Long questionId);
}
