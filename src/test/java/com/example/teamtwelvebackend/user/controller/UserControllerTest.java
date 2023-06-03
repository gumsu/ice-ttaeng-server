package com.example.teamtwelvebackend.user.controller;

import com.example.teamtwelvebackend.user.domain.dtos.SignUpRequestDto;
import com.example.teamtwelvebackend.user.domain.entities.User;
import com.example.teamtwelvebackend.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {
    @TestConfiguration
    static class TestWebSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable();
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 api")
    void signUp() throws Exception {

        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
            .email("test@test.com")
            .password("1234")
            .nickname("testuser")
            .build();

        given(userService.signUp(any(User.class))).willReturn(Long.valueOf(1));

        // when
        ResultActions result = mockMvc.perform(post("/api/users/signup")
            .content(objectMapper.writeValueAsString(signUpRequestDto))
            .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
            .andDo(document("user-signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("nickname").description("닉네임"))))
            .andDo(print());
    }
}
