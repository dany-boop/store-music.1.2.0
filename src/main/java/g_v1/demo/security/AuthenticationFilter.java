package g_v1.demo.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import g_v1.demo.model.entity.User;
import g_v1.demo.service.JWTService;
import g_v1.demo.service.UserService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
            if (bearerToken != null && jwtService.isTokenBlackListed(bearerToken)) {
                DecodedJWT payload = jwtService.verifyToken(bearerToken);
                User user = userService.getOne(payload.getSubject());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {

        }
        filterChain.doFilter(req, res);
    }

}
