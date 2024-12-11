package g_v1.demo.dto.res;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustRes extends BaseRes {
    private String name;

    private String email;

    private String phone;

    private UserRes user;
}
