package com.example.teamtwelvebackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@SpringBootApplication
@Slf4j
public class TeamtwelveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamtwelveBackendApplication.class, args);
    }


    /**
     * 현재 설정되어 있는 환경 변수 값을 확인하기 위한 로그 출력 코드 입니다.
     */
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final Environment env = event.getApplicationContext()
                .getEnvironment();

        log.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));

        final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();

        StreamSupport.stream(sources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
                .forEach(prop -> log.info("{}: {}", prop, env.getProperty(prop)));
    }
}
