package com.example.teamtwelvebackend.activity.thankcircle.controller.ws;

import com.example.teamtwelvebackend.activity.thankcircle.service.TcGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TcGuestController {
    final TcGuestService tcGuestService;

}