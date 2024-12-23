package g_v1.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import g_v1.demo.constant.Constant;
import g_v1.demo.dto.req.AuthReq;
import g_v1.demo.dto.res.AuthRes;
import g_v1.demo.service.AuthService;
import g_v1.demo.util.ResUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = Constant.AUTH_API_URL)

public class AuthController {
    private final AuthService authService;

    @Value("${app.music-store.refresh-token-in-seconds}")
    private Integer REFRESH_TOKEN_EXPIRATION_IN_SECONDS;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthReq authReq, HttpServletResponse response) {

        AuthRes login = authService.login(authReq);
        setCookie(response, login.getRefreshToken());

        return ResUtil.buildRes(HttpStatus.OK, Constant.SUCCESS_LOGIN_MSG, login);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthReq authReq) {
        return ResUtil.buildRes(HttpStatus.OK, Constant.SUCCESS_REGISTER_MSG, authService.register(authReq));
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(Constant.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * REFRESH_TOKEN_EXPIRATION_IN_SECONDS);
        response.addCookie(cookie);
    }

}
