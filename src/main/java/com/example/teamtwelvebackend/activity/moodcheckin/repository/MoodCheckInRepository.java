package com.example.teamtwelvebackend.activity.moodcheckin.repository;

import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckIn;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodCheckInRepository extends JpaRepository<MoodCheckIn, Long> {

    Optional<MoodCheckIn> findBySessionId(String sessionId);
}
