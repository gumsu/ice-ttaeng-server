package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

/**
 * 스피드게임 정답 제출
 *
 * @param questionId 문제 아이디
 * @param answerId 정답 아이디
 */
public record SubmitAnswer(
        Long questionId,
        Long answerId) {
}
