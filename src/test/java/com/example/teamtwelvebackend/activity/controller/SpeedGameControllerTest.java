package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.speedgame.service.HostService;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomDto;
import com.example.teamtwelvebackend.activity.speedgame.service.GuestService;
import com.example.teamtwelvebackend.activity.speedgame.controller.rest.SpeedGameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @MockBean
    HostService service;

    @MockBean
    GuestService guestService;

    @Test
    @DisplayName("스피드게임 방 만들기")
    void createRoom() throws Exception {
        String userName = "google_118339889321875083261";
        String userId = "204c3264-77d5-4ac7-b776-4be9921535ee";
        Consumer<Jwt.Builder> jwtConsumer = jwt -> jwt
                .claim("username", userName)
                .claim("sub", userId);

        String request = """
                {
                    "questions": [
                        {
                            "number": 1,
                            "question_text": "질문 내용",
                            "answers": [
                                {
                                    "number": 1,
                                    "answer_text": "대답 1",
                                    "correct_answer": true
                                },
                                {
                                    "number": 2,
                                    "answer_text": "대답 2",
                                    "correct_answer": false
                                },
                                {
                                    "number": 3,
                                    "answer_text": "대답 3",
                                    "correct_answer": false
                                }
                            ]
                        },
                        {
                            "number": 2,
                            "question_text": "질문 내용 2",
                            "answers": [
                                {
                                    "number": 1,
                                    "answer_text": "대답 2-1",
                                    "correct_answer": false
                                },
                                {
                                    "number": 2,
                                    "answer_text": "대답 2-2",
                                    "correct_answer": true
                                }
                            ]
                        }
                    ]
                }
                """;


        when(service.createRoom(eq(userId), Mockito.any()))
                .thenReturn(new RoomCreatedDto("sample-room", "sample-room"));

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/activity/speedgame")
                        .header("Authorization", "Bearer access_token")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwtConsumer))
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
                                fieldWithPath("questions.[].number").description("문제 순서"),
                                fieldWithPath("questions.[].question_text").description("문제 내용"),
                                fieldWithPath("questions.[].answers.[].number").description("대답 순서"),
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

    @Test
    @DisplayName("스피드게임 방 정보 조회")
    void roomInfo() throws Exception {
        String roomName = UUID.randomUUID().toString();
        when(guestService.getRoomDtoByName(eq(roomName)))
                .thenReturn(new RoomDto(roomName, roomName, "short-url", "qr-url", 1));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.
                get("/activity/speedgame/{roomName}", roomName));

        // then
        result.andExpect(status().isOk())
                .andDo(document("speedgame-get-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("roomName").description("액티비티 방 이름")),
                        responseFields(
                                fieldWithPath("room_name").description("액티비티 방 이름"),
                                fieldWithPath("room_code").description("액티비티 진입 코드"),
                                fieldWithPath("participant_count").description("현재 방 참가 인원 수"),
                                fieldWithPath("qr_code_image_url").description("QRcode 이미지 URL"),
                                fieldWithPath("short_url").description("단축 URL")
                        )
                        )
                )
                .andDo(print());
    }
}