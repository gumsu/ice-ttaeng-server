package com.example.teamtwelvebackend.activity.common.controller;

import com.example.teamtwelvebackend.activity.common.controller.response.WebSocketErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity handleBadRequest(HttpServletRequest req, Exception ex) {
        return ResponseEntity.notFound().build();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public WebSocketErrorResponse error(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new WebSocketErrorResponse(ex.getMessage());
    }
}
