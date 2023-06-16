package com.example.teamtwelvebackend.activity.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(SpeedGameController.class)
@ActiveProfiles("test")
class SpeedGameControllerTest {
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

    @Test
    @DisplayName("스피드게임 방 만들기")
    void createRoom() throws Exception {

        ResultActions result = mockMvc.perform(post("/activity/speedgame")
                        .header("Authorization", "Bearer access_token")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt
                                        .claim("username", "google_118339889321875083261")
                                        .claim("sub", "204c3264-77d5-4ac7-b776-4be9921535ee")
                                )
                        )
                .content(request)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("speedgame-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("access token 이 필요합니다")),
                        requestFields(
                                fieldWithPath("questions.[].order").description("문제 순서"),
                                fieldWithPath("questions.[].question_text").description("문제 내용"),
                                fieldWithPath("questions.[].answers.[].order").description("대답 순서"),
                                fieldWithPath("questions.[].answers.[].answer_text").description("대답 내용"),
                                fieldWithPath("questions.[].answers.[].correct_answer").description("정답 여부")
                        ),
                        responseFields(
                                fieldWithPath("room_name").description("액티비티 방 이름"),
                                fieldWithPath("room_code").description("액티비티 진입 코드")
                        )
                        )
                )
                .andDo(print());
    }

    String request = """
                {
                    "questions": [
                        {
                            "order": 1,
                            "question_text": "질문 내용",
                            "answers": [
                                {
                                    "order": 1,
                                    "answer_text": "대답 1",
                                    "correct_answer": true
                                },
                                {
                                    "order": 2,
                                    "answer_text": "대답 2",
                                    "correct_answer": false
                                },
                                {
                                    "order": 3,
                                    "answer_text": "대답 3",
                                    "correct_answer": false
                                },
                                {
                                    "order": 4,
                                    "answer_text": "대답 4",
                                    "correct_answer": false
                                },
                                {
                                    "order": 5,
                                    "answer_text": "대답 5",
                                    "correct_answer": false
                                }
                            ]
                        },
                        {
                            "order": 2,
                            "question_text": "질문 내용 2",
                            "answers": [
                                {
                                    "order": 1,
                                    "answer_text": "대답 2-1",
                                    "correct_answer": false
                                },
                                {
                                    "order": 2,
                                    "answer_text": "대답 2-2",
                                    "correct_answer": false
                                },
                                {
                                    "order": 3,
                                    "answer_text": "대답 2-3",
                                    "correct_answer": false
                                },
                                {
                                    "order": 4,
                                    "answer_text": "대답 2-4",
                                    "correct_answer": false
                                },
                                {
                                    "order": 5,
                                    "answer_text": "대답 2-5",
                                    "correct_answer": true
                                }
                            ]
                        }
                    ]
                }
                """;
}