package com.example.teamtwelvebackend.activity.repository;

import com.example.teamtwelvebackend.activity.domain.SpeedGameAnswerEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpeedGameAnswerRepository {
    Map<Long, SpeedGameAnswerEntity> repository = new HashMap<>();

    public void submitAnswer(String roomName, String userId, String questionId, String answerId) {
        SpeedGameAnswerEntity entity = new SpeedGameAnswerEntity(roomName, userId, questionId, answerId);
        Long id = repository.size() + 1L;
        repository.put(id, entity);
    }

    public List<SpeedGameAnswerEntity> getSubmittedAnswers(String roomName, Integer currentQuestion) {
        return repository.entrySet().stream()
                .filter(entry -> {
                    entry.getValue();
                    return true;
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
