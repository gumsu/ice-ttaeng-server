package com.example.teamtwelvebackend.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.teamtwelvebackend.user.domain.dtos.SignUpRequestDto;
import com.example.teamtwelvebackend.user.domain.entities.User;
import com.example.teamtwelvebackend.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
            .email("test@test.com")
            .password("1234")
            .nickname("testuser")
            .build();

        user = signUpRequestDto.toEntity();
    }

    @Test
    @DisplayName("중복되는 이메일이 없는 경우 회원가입을 할 수 있다.")
    void signUpUser() {
        // given
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        Long userId = userService.signUp(user);

        // then
        then(userRepository).should().save(user);
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("중복되는 이메일이 있는 경우 회원가입을 할 수 없다.")
    void signUpExitEmailUser() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        // expected
        assertThatThrownBy(() -> userService.signUp(user))
            .isInstanceOf(IllegalArgumentException.class);

        then(userRepository).should().findByEmail(user.getEmail());
        then(userRepository).should(never()).save(user);
    }
}
