package com.example.teamtwelvebackend.activity.mininetworking.service;

import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingRoom;
import com.example.teamtwelvebackend.activity.mininetworking.repository.MiniNetworkingRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MiniNetworkingService {

    private final MiniNetworkingRoomRepository miniNetworkingRoomRepository;

    @Transactional
    public MiniNetworkingRoom createRoom(String userName) {
        MiniNetworkingRoom miniNetworkingRoom = MiniNetworkingRoom.builder()
            .createdBy(userName)
            .build();
        return miniNetworkingRoomRepository.save(miniNetworkingRoom);
    }

    public MiniNetworkingRoom getRoomByName(String roomName) {
        return miniNetworkingRoomRepository.findByName(roomName)
            .orElseThrow(() -> new IllegalStateException("없는 방입니다."));
    }
}
