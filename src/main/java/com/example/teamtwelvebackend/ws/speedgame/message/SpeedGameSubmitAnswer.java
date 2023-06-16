package com.example.teamtwelvebackend.ws.speedgame.message;

/**
 * 스피드게임 정답 제출
 *
 * @param userId 제출자 아이디
 * @param questionId 문제 아이디
 * @param answerId 정답 아이디
 */
public record SpeedGameSubmitAnswer(
        String userId,
        String questionId,
        String answerId) {
}
