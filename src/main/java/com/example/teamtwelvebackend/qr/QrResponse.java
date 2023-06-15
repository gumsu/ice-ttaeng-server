package com.example.teamtwelvebackend.qr;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QrResponse {

    private String url;
    private String qr;
}
