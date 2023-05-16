package com.example.teamtwelvebackend.user.controller;

import com.example.teamtwelvebackend.user.domain.dtos.SignUpRequestDto;
import com.example.teamtwelvebackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.teamtwelvebackend.user.controller.UserController.USER_API_URI;

@RequestMapping(USER_API_URI)
@RequiredArgsConstructor
@RestController
public class UserController {

    public static final String USER_API_URI = "/api/users";

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto.toEntity());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
