package g_v1.demo.service;

// import org.springframework.data.domain.Page;

import g_v1.demo.dto.req.UserChangePasswordReq;
import g_v1.demo.dto.req.UserReq;
import g_v1.demo.dto.res.UserRes;
import g_v1.demo.model.entity.User;

public interface UserService {
    User getOne(String id);

    UserRes create(UserReq userReq);

    User findByUsername(String username);

    UserRes update(UserReq userReq);

    void delete(String id);

    // Page<UserRes> findAllUser(UserSearchRequest userSearchRequest);
    UserRes changePassword(String id, UserChangePasswordReq req);

    UserRes getUserDetails(String id);
}
