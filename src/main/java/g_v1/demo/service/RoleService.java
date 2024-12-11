package g_v1.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import g_v1.demo.dto.req.PageReq;
import g_v1.demo.dto.res.RoleRes;
import g_v1.demo.model.entity.User;
import g_v1.demo.model.enums.UserRole;

public class RoleService {
    List<RoleRes> getAll();

    RoleRes getByName(String name);

    UserRole getOne(String name);

}
