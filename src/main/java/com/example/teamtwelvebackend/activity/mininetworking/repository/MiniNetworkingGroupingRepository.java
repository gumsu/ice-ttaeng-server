package com.example.teamtwelvebackend.activity.mininetworking.repository;

import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingGrouping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniNetworkingGroupingRepository extends JpaRepository<MiniNetworkingGrouping, Long> {

    List<MiniNetworkingGrouping> findByRoomName(String roomName);

}
