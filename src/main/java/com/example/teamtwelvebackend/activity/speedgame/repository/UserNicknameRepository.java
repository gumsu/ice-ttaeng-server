package com.example.teamtwelvebackend.activity.speedgame.repository;

import com.example.teamtwelvebackend.activity.speedgame.domain.UserNickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNicknameRepository extends JpaRepository<UserNickname, Long> {
    List<UserNickname> findByRoomNameAndSessionIdIn(String roomName, List<String> sessionId);
}
