package com.example.teamtwelvebackend.qr;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortURLAndQrVO {

    private final String url;
    private final String qr;
}
