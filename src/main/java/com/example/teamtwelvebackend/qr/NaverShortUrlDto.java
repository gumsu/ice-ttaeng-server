package com.example.teamtwelvebackend.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NaverShortUrlDto {

    private Result result;
    private String message;
    private String code;

    @Getter
    public static class Result {
        @JsonProperty("url")
        private String url;
        @JsonProperty("hash")
        private String hash;
        @JsonProperty("orgUrl")
        private String orgUrl;
    }
}
