package g_v1.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import g_v1.demo.constant.Constant;
import g_v1.demo.service.AuthService;
import lombok.*;

// @Tag(name = "Authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = Constant.AUTH_API_URL)

public class AuthController {
    private final AuthService authService;

    @Value("${app.music-store.refresh-token-in-seconds}")
    private Integer REFRESH_TOKEN_EXPIRATION_IN_SECONDS;

    
}
