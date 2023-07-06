package com.example.teamtwelvebackend.activity.moodcheckin.repository;

import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInUserNickname;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodCheckInUserNicknameRepository extends JpaRepository<MoodCheckInUserNickname, Long> {

    Optional<MoodCheckInUserNickname> findBySessionId(String sessionId);

    Integer countByRoomNameAndMoodNot(String roomName, Integer mood);

    @Query("SELECT AVG(m.mood) FROM MoodCheckInUserNickname m WHERE m.roomName = :room_name AND m.mood <> 0")
    Double getAverageStatusForNonZero(@Param("room_name") String roomName);

    @Query(value = "SELECT * FROM mc_user_nickname WHERE room_name = :room_name AND mood <> 0 ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    MoodCheckInUserNickname getRandomGuest(@Param("room_name") String roomName);
}
