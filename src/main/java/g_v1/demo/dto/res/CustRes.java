package g_v1.demo.dto.res;

import g_v1.demo.model.enums.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustRes extends BaseRes {
    private String username;

    private String email;

    private String phone;

    private UserRes user;

    private UserRole role;
}
