package com.example.teamtwelvebackend;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QrResponse {

    private String url;
    private String qr;
}
