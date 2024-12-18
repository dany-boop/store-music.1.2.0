package g_v1.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;

import g_v1.demo.model.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
    public static void info(String message) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User: {} {}", user.getUsername(), message);
    }

    public static void error(String message) {
        log.error(message);
    }
}
