package com.example.teamtwelvebackend;

import com.example.teamtwelvebackend.activity.speedgame.controller.ws.Participant;
import com.example.teamtwelvebackend.ws.StompErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${websocket.allow-origin-patterns}")
    private List<String> originPatterns;

    final JwtAuthenticationProvider jwtAuthenticationProvider;

    public WebSocketConfig(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(originPatterns.toArray(String[]::new))
                .setHandshakeHandler(new AssignPrincipalHandshakeHandler())
                .withSockJS();
        registry.setErrorHandler(new StompErrorHandler());
    }

    public static class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Participant determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            return new Participant(UUID.randomUUID().toString());
        }
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");
                    String token = authorization.get(0);

                    if (!token.isEmpty()) {
                        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = new BearerTokenAuthenticationToken(token);
                        Authentication authenticate = jwtAuthenticationProvider.authenticate(bearerTokenAuthenticationToken);
                        accessor.setUser(authenticate);
                    }

                }

                // 참가자가 토픽을 구독할 때 닉네임이 있다면 등록 가능
                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) &&
                    accessor.getHeader("simpUser") instanceof Participant participant) {
                    List<String> header = accessor.getNativeHeader("nickname");
                    if (header != null && !header.isEmpty()) {
                        String nickname = header.get(0);
                        participant.setNickname(nickname);
                    }
                }

                return message;
            }
        });
    }
}
