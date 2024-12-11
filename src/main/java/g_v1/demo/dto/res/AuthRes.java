package g_v1.demo.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRes {
    private String id;

    private String role;

    private String token;

    private String refreshToken;
}
