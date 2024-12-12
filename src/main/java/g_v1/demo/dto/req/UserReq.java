package g_v1.demo.dto.req;

import lombok.*;
import g_v1.demo.model.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReq {
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private UserRole role;
}
