package com.example.teamtwelvebackend.activity.speedgame.repository;

import com.example.teamtwelvebackend.activity.speedgame.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByRoomNameAndNumber(String roomName, int number);
}
