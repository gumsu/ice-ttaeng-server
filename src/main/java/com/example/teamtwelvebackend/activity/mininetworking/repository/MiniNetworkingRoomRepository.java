package com.example.teamtwelvebackend.activity.mininetworking.repository;

import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniNetworkingRoomRepository extends JpaRepository<MiniNetworkingRoom, Long> {

    Optional<MiniNetworkingRoom> findByName(String roomName);
}
