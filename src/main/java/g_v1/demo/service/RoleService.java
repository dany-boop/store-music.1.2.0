package g_v1.demo.service;

import java.util.List;

import g_v1.demo.dto.res.RoleRes;
import g_v1.demo.model.enums.UserRole;

public interface RoleService {
    List<RoleRes> getAll();

    RoleRes getByName(String name);

    UserRole getOne(String name);

}
