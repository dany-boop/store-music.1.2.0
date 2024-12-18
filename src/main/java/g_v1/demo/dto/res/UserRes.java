package g_v1.demo.dto.res;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRes extends BaseRes {
    private String id;
    private String name;
    private String email;
    private String username;
    private String role;
}
