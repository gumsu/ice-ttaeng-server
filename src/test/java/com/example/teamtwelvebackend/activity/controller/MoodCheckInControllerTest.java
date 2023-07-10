package com.example.teamtwelvebackend.activity.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.teamtwelvebackend.activity.moodcheckin.controller.rest.MoodCheckInController;
import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInRoom;
import com.example.teamtwelvebackend.activity.moodcheckin.service.MoodCheckInService;
import com.example.teamtwelvebackend.qr.NaverShortUrlService;
import com.example.teamtwelvebackend.qr.ShortURLAndQrVO;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt.Builder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MoodCheckInController.class)
@ActiveProfiles("test")
class MoodCheckInControllerTest {

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
    private MoodCheckInService moodCheckInService;

    @MockBean
    private NaverShortUrlService naverShortUrlService;

    @Test
    @DisplayName("기분체크인 방 만들기")
    void createRoom() throws Exception {
        String userName = "google_118339889321875083261";
        String userId = "204c3264-77d5-4ac7-b776-4be9921535ee";
        Consumer<Builder> jwtConsumer = jwt -> jwt
            .claim("username", userName)
            .claim("sub", userId);

        when(moodCheckInService.createRoom(eq(userId)))
            .thenReturn(new MoodCheckInRoom("sample-user"));

        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/activity/moodcheckin")
                .header("Authorization", "Bearer access_token")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwtConsumer)));

        // then
        result.andExpect(status().isOk())
            .andDo(document("moodcheckin-create",
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
    @DisplayName("기분체크인 방 조회")
    void roomInfo() throws Exception {
        String roomName = UUID.randomUUID().toString();
        when(moodCheckInService.getRoomByName(roomName)).thenReturn(new MoodCheckInRoom("user"));
        when(naverShortUrlService.createShortURLAndQrCode("https://bside1512.dev/moodcheckin/" + roomName))
            .thenReturn(ShortURLAndQrVO.builder()
                .url("short_url")
                .qr("qr-code")
                .build());

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.
            get("/activity/moodcheckin/{roomName}", roomName));

        result.andExpect(status().isOk())
            .andDo(document("moodcheckin-get-info",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(parameterWithName("roomName").description("액티비티 방 이름")),
                    responseFields(
                        fieldWithPath("room_name").description("액티비티 방 이름"),
                        fieldWithPath("room_code").description("액티비티 진입 코드"),
                        fieldWithPath("qr_code_image_url").description("QRcode 이미지 URL"),
                        fieldWithPath("short_url").description("단축 URL")
                    )
                )
            )
            .andDo(print());
    }
}
