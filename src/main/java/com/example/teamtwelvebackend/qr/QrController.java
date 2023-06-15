package com.example.teamtwelvebackend.qr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QrController {

    private final NaverShortUrlService naverShortUrlService;

    @GetMapping("/qr")
    public ResponseEntity<QrResponse> createQrCode(@RequestParam String url) {

        NaverShortUrlDto naverShortUrlDto = naverShortUrlService.naverShortUrlApi(url);
        String shortUrl = naverShortUrlDto.getResult().getUrl();
        String qr = shortUrl + ".qr";
        QrResponse qrResponse = QrResponse.builder()
            .url(shortUrl)
            .qr(qr)
            .build();
        return ResponseEntity.ok().body(qrResponse);
    }
}
