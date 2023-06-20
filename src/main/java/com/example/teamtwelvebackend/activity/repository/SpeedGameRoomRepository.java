package com.example.teamtwelvebackend.activity.repository;

import com.example.teamtwelvebackend.activity.domain.SpeedGameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeedGameRoomRepository extends JpaRepository<SpeedGameRoom, Long> {
    Optional<SpeedGameRoom> findByName(String roomName);
}
