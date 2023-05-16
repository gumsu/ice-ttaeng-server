package com.example.teamtwelvebackend.user.service;

import com.example.teamtwelvebackend.user.domain.entities.User;
import com.example.teamtwelvebackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signUp(User user) {
        validateSignUpUser(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateSignUpUser(User user) {
        userRepository.findByEmail(user.getEmail())
            .ifPresent( existingUser -> {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            });
    }
}
