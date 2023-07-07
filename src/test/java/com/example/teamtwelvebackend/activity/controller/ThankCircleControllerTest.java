package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.thankcircle.service.TcGuestService;
import com.example.teamtwelvebackend.activity.thankcircle.service.TcHostService;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomDto;
import com.example.teamtwelvebackend.activity.thankcircle.controller.rest.ThankCircleController;
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

import java.util.NoSuchElementException;
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
@WebMvcTest(ThankCircleController.class)
@ActiveProfiles("test")
class ThankCircleControllerTest {
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
    TcHostService service;

    @MockBean
    TcGuestService guestService;

    @Test
    @DisplayName("감사 서클 방 만들기")
    void createRoom() throws Exception {
        String userName = "google_118339889321875083261";
        String userId = "204c3264-77d5-4ac7-b776-4be9921535ee";
        Consumer<Jwt.Builder> jwtConsumer = jwt -> jwt
                .claim("username", userName)
                .claim("sub", userId);

        when(service.createRoom(eq(userId)))
                .thenReturn(new RoomCreatedDto("sample-room", "sample-room"));

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/activity/thankcircle")
                        .header("Authorization", "Bearer access_token")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwtConsumer))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("thankcircle-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("access token 이 필요합니다")),
                        responseFields(
                                fieldWithPath("room_name").description("액티비티 방 이름"),
                                fieldWithPath("room_code").description("액티비티 진입 코드")
                        )
                        )
                )
                .andDo(print());
    }

    @Test
    @DisplayName("감사 서클 방 정보 조회")
    void roomInfo() throws Exception {
        String roomName = UUID.randomUUID().toString();
        when(guestService.getRoomDtoByName(eq(roomName)))
                .thenReturn(new RoomDto(roomName, roomName, "short-url", "qr-url", 1));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.
                get("/activity/thankcircle/{roomName}", roomName));

        // then
        result.andExpect(status().isOk())
                .andDo(document("thankcircle-get-info",
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


    @Test
    @DisplayName("감사 서클 방 정보를 찾을 수 없는 경우")
    void notFoundRoom() throws Exception {
        String roomName = "does-not-exist-id";
        when(guestService.getRoomDtoByName(eq(roomName)))
                .thenThrow(NoSuchElementException.class);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.
                get("/activity/thankcircle/{roomName}", roomName));

        // then
        result.andExpect(status().isNotFound())
                .andDo(document("thankcircle-get-info-not-found",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andDo(print());
    }
}