package g_v1.demo.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {
    private String id;
    private String name;
    private String email;
    private String username;
    private String role;
}
