package g_v1.demo.dto.req;

import lombok.*;

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
    private String role;
}
