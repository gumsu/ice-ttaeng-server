package com.example.teamtwelvebackend.activity.thankcircle.repository;

import com.example.teamtwelvebackend.activity.thankcircle.domain.ThankCircleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TcRoomRepository extends JpaRepository<ThankCircleRoom, Long> {
    Optional<ThankCircleRoom> findByName(String roomName);
}
