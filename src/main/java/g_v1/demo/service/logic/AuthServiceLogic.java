package g_v1.demo.service.logic;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import g_v1.demo.constant.Constant;
import g_v1.demo.dto.req.AuthReq;
import g_v1.demo.dto.req.CustReq;
import g_v1.demo.dto.req.UserReq;
import g_v1.demo.dto.res.AuthRes;
import g_v1.demo.dto.res.CustRes;
import g_v1.demo.dto.res.UserRes;
import g_v1.demo.model.entity.User;
import g_v1.demo.model.enums.UserRole;
import g_v1.demo.service.AuthService;
import g_v1.demo.service.JWTService;
import g_v1.demo.service.RefreshTokenService;
import g_v1.demo.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceLogic implements AuthService {
    private final JWTService jwtService;
    // private final RoleSer
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthRes login(AuthReq authRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createToken(user.getId());
        return AuthRes.builder()
                .id(user.getId())
                .token(accessToken)
                .role(user.getRole().getDescription())
                .refreshToken(refreshToken)
                // .createdAt(user.getCreatedAt())
                // .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public CustRes register(AuthReq req) {
        UserReq userReq = UserReq.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .role(UserRole.User) // Assign the role as Customer
                .build();

        UserRes userRes = userService.create(userReq);

        CustReq custreq = CustReq.builder()
                .userId(userRes.getId())
                .build();

        return customerService.create(customerReq);

    }

    @Override
    public AuthRes refreshToken(String token) {
        String userId = refreshTokenService.getUserIdByToken(token);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_REFRESH_TOKEN_INVALID_MSG);
        }
        User user = userService.getOne(userId);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(userId);
        String newToken = jwtService.generateToken(user);
        return AuthRes.builder()
                .id(user.getId())
                .token(newToken)
                .refreshToken(newRefreshToken)
                .role(user.getRole().getDescription())
                .build();
    }

    @Override
    public void logout(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        refreshTokenService.deleteRefreshToken(user.getId());
        jwtService.blacklistAccessToken(token);
    }

}
