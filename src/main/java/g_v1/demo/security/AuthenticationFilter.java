package g_v1.demo.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import g_v1.demo.model.entity.User;
import g_v1.demo.service.JWTService;
import g_v1.demo.service.UserService;
import org.springframework.lang.NonNull;
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
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log error (best practice)
            logger.error("Authentication error: ", e);
            e.printStackTrace();
        }

        // @Override
        // protected void doFilterInternal(
        // @NonNull HttpServletRequest req,
        // @NonNull HttpServletResponse res,
        // @NonNull FilterChain filterChain) throws ServletException, IOException {
        // try {
        // final String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);

        // if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        // filterChain.doFilter(req, res);
        // return;
        // }

        // final String jwt = authHeader.substring(7); // Remove "Bearer " prefix

        // // Don't authenticate if token is blacklisted
        // if (jwtService.isTokenBlackListed(jwt)) {
        // filterChain.doFilter(req, res);
        // return;
        // }

        // // Verify token and get user details
        // DecodedJWT decodedJWT = jwtService.verifyToken(jwt);
        // if (decodedJWT != null &&
        // SecurityContextHolder.getContext().getAuthentication() == null) {
        // User user = userService.getOne(decodedJWT.getSubject());

        // // Create authentication token
        // UsernamePasswordAuthenticationToken authToken = new
        // UsernamePasswordAuthenticationToken(
        // user,
        // null,
        // user.getAuthorities());

        // authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        // SecurityContextHolder.getContext().setAuthentication(authToken);
        // }
        // } catch (Exception e) {
        // logger.error("Authentication error: ", e);
        // }
        filterChain.doFilter(req, res);
    }

}
