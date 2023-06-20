package com.example.teamtwelvebackend.activity.repository;

import com.example.teamtwelvebackend.activity.domain.SpeedGameQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeedGameQuestionRepository extends JpaRepository<SpeedGameQuestion, Long> {
}
