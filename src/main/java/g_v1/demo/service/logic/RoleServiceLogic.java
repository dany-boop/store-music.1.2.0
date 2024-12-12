package g_v1.demo.service.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import g_v1.demo.constant.Constant;
// import g_v1.demo.dto.req.RoleReq;
import g_v1.demo.dto.res.RoleRes;
import g_v1.demo.model.enums.UserRole;
import g_v1.demo.service.RoleService;
// import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceLogic implements RoleService {

    @Override
    public List<RoleRes> getAll() {
        return Arrays.stream(UserRole.values())
                .map(role -> RoleRes.builder()
                        .name(role.name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public RoleRes getByName(String name) {
        try {
            UserRole role = UserRole.valueOf(name.toUpperCase());
            return RoleRes.builder()
                    .name(role.name())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_GET_ROLE_MSG);
        }
    }

    @Override
    public UserRole getOne(String name) {
        try {
            return UserRole.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_GET_ROLE_MSG);
        }
    }

}
