package g_v1.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/start")
public class start {

    @GetMapping
    public String startWelcome() {
        return "Project Initialized";
    }
}
