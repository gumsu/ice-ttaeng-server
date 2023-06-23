package com.example.teamtwelvebackend.activity.moodcheckin.repository;

import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodCheckInRoomRepository extends JpaRepository<MoodCheckInRoom, Long> {

    Optional<MoodCheckInRoom> findByName(String name);
}
