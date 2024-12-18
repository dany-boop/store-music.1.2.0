package g_v1.demo.service.logic;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import g_v1.demo.constant.Constant;
import g_v1.demo.service.RedisService;
import g_v1.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceLogic implements RefreshTokenService {

    private final RedisService redisService;

    @Value("${app.store-music.refresh-token-expiration-in-seconds}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_SECONDS;

    @Override
    public String generateRefreshToken(String userId) {
        String refreshToken = UUID.randomUUID().toString();
        if (redisService.isExist("refreshToken:" + userId))
            deleteRefreshToken(userId);
        redisService.save("refreshToken:" + userId, refreshToken,
                Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_IN_SECONDS));
        redisService.save("userId:" + refreshToken, userId, Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_IN_SECONDS));
        return refreshToken;
    }

    @Override
    public void deleteRefreshToken(String userId) {
        String refreshToken = redisService.get("refreshToken:" + userId);
        redisService.delete("refreshToken:" + userId);
        redisService.delete("userId:" + refreshToken);
    }

    @Override
    public String rotateRefreshToken(String userId) {
        deleteRefreshToken(userId);
        return generateRefreshToken(userId);
    }

    @Override
    public String getUserIdByToken(String refreshToken) {
        if (!redisService.isExist("userId:" + refreshToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.ERROR_REFRESH_TOKEN_INVALID_MSG);
        return redisService.get("userId:" + refreshToken);
    }
}
