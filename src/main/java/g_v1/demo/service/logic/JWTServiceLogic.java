package g_v1.demo.service.logic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import g_v1.demo.constant.Constant;
import g_v1.demo.model.entity.User;
import g_v1.demo.service.JWTService;
import g_v1.demo.service.RedisService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JWTServiceLogic implements JWTService {

    private final RedisService redisService;

    private final String BLACKLISTED = "BLACKLISTED";

    @Value("${app.store-music.jwt-secret}")
    private String JWT_SECRET;

    @Value("${app.store-music.jwt-expiration-in-seconds}")
    private Integer JWT_EXPIRATION_IN_SECONDS;

    @Value("${app.store-music.jwt-issuer}")
    private String JWT_ISSUER;

    @Override
    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getId())
                    .withClaim("role", user.getRole().name())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(JWT_EXPIRATION_IN_SECONDS, ChronoUnit.SECONDS))
                    .withIssuer(JWT_ISSUER)
                    .sign(Algorithm.HMAC256(JWT_SECRET));
        } catch (JWTCreationException e) {
            throw new RuntimeException(Constant.ERROR_GENERATE_TOKEN_MSG);
        }
    }

    @Override
    public DecodedJWT verifyToken(String bearerToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET))
                    .withIssuer(JWT_ISSUER)
                    .build();
            return verifier.verify(parseToken(bearerToken));
        } catch (JWTVerificationException e) {
            throw new RuntimeException(Constant.ERROR_VERIFY_TOKEN_MSG);
        }
    }

    @Override
    public void blacklistAccessToken(String bearerToken) {
        DecodedJWT decodedJWT = verifyToken(bearerToken);
        Date expiresAt = decodedJWT.getExpiresAt();
        long timeLeft = expiresAt.getTime() - System.currentTimeMillis();
        redisService.save(parseToken(bearerToken), BLACKLISTED, Duration.ofMillis(timeLeft));
    }

    @Override
    public boolean isTokenBlackListed(String bearerToken) {
        String blacklistedToken = redisService.get(parseToken(bearerToken));
        return blacklistedToken != null && blacklistedToken.equals(BLACKLISTED);
    }

    private String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7);
        return null;
    }
}
