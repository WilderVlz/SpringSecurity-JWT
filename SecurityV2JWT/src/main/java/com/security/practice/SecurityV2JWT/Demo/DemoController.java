package com.security.practice.SecurityV2JWT.Demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {
    
    @PostMapping(value = "demo")
    public String welcome()
    {
        return "welcome from secure endpoint";
    }
    
}
