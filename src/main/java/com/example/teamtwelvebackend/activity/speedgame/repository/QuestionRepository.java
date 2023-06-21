package com.example.teamtwelvebackend.activity.speedgame.repository;

import com.example.teamtwelvebackend.activity.speedgame.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByRoomNameAndNumber(String roomName, int number);
}
