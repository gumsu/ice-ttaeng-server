package com.example.teamtwelvebackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.messaging.web.csrf.CsrfChannelInterceptor;
import org.springframework.security.messaging.web.csrf.XorCsrfChannelInterceptor;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    /**
     * 웹소켓 csrf 설정을 임시로 해제함
     *
     * @return
     */
    @Bean
    ChannelInterceptor csrfChannelInterceptor() {
//        return new XorCsrfChannelInterceptor(); // default csrf interceptor
        return new ChannelInterceptor() {};
    }


    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .nullDestMatcher().permitAll()
                .simpMessageDestMatchers("/app/speedgame/*/start").authenticated()
                .simpMessageDestMatchers("/app/thankcircle/*/start").authenticated()
                .anyMessage().permitAll();

        return messages.build();
    }

}
