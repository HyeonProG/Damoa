package com.damoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebRTCController {

    @GetMapping("/webRTC")
    public String rtcPage() {
        return "/webRTC/indexRTC";
    }
}
