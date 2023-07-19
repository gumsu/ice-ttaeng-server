package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.common.controller.ActivityController;
import com.example.teamtwelvebackend.activity.common.domain.Activity;
import com.example.teamtwelvebackend.activity.common.service.ActivityService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ActivityController.class)
@ActiveProfiles("test")
class ActivityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @TestConfiguration
    static class TestWebSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable();
            return http.build();
        }
    }

    @Test
    @DisplayName("액티비티 전체 조회")
    void list() throws Exception {

        // given
        List<Activity> list = List.of(
            new Activity(1L, 1,"스피드게임", "자유 퀴즈를 만들고 정답을 맞혀보세요", LocalDateTime.now()),
            new Activity(2L, 2,"두 개의 진실 하나의 거짓말", "세 가지 정보 중 하나의 거짓을 찾아보세요", LocalDateTime.now()),
            new Activity(3L, 3,"감사 서클", "참여자의 이름을 적어주세요", LocalDateTime.now()),
            new Activity(4L, 4,"기분 체크인", "참여자의 오늘의 기분을 알아보세요!", LocalDateTime.now()),
            new Activity(5L, 5,"미니 네트워킹", "그룹을 구성하여 네트워킹을 시작하세요", LocalDateTime.now()),
            new Activity(6L, 6,"이미지 게임", "가장 ~ 할 것 같은 사람은?", LocalDateTime.now())
        );
        BDDMockito.given(activityService.getAllActivity()).willReturn(list);
        ResultActions result = mockMvc.perform(get("/activities"));

        // then
        result.andExpect(status().isOk())
                .andDo(document("activity-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].activity_id").description("액티비티 아이디"),
                                fieldWithPath("[].display_name").description("액티비티 제목"),
                                fieldWithPath("[].description").description("액티비티 설명")
                        )
                    )
                )
                .andDo(print());
    }
}
