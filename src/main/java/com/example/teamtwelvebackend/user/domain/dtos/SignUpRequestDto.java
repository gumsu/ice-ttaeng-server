package com.example.teamtwelvebackend.user.domain.dtos;

import com.example.teamtwelvebackend.user.domain.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignUpRequestDto {

    @NotEmpty(message = "이메일 입력은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    private String password;

    @NotEmpty(message = "닉네임 입력은 필수입니다.")
    private String nickname;

    @Builder
    public SignUpRequestDto(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity() {
        return User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
