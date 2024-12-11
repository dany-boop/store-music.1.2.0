package g_v1.demo.service;

import g_v1.demo.dto.req.AuthReq;
import g_v1.demo.dto.res.AuthRes;

public interface AuthService {
    AuthRes login(AuthReq req);

    void logout(String bearerToken);

    AuthRes refreshToken(String refreshToken);
}
