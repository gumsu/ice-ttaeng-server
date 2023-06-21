package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

/**
 * 액티비티 진행 중 주고 받는 메시지
 *
 * @param type 메시지 타입
 * @param message 메시지 내용
 */
public record ActivityRoomMessage(
        String type,
        String message,
        Object payload) {
}
