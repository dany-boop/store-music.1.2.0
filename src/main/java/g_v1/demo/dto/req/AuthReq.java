package g_v1.demo.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthReq {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

}
