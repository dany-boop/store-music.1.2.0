package g_v1.demo.service;

import com.auth0.jwt.interfaces.DecodedJWT;

import g_v1.demo.model.entity.User;

public interface JWTService {

    String generateToken(User user);

    DecodedJWT verifyToken(String bearerToken);

    void blacklistAccessToken(String bearerToken);

    boolean isTokenBlackListed(String bearerToken);
}
