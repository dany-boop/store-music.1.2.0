package g_v1.demo.dto.res;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRes extends BaseRes {
    private String id;

    private String role;

    private String token;

    private String refreshToken;
}
