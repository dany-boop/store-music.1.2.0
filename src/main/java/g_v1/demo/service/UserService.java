package g_v1.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import g_v1.demo.dto.req.PageReq;

// import org.springframework.data.domain.Page;

import g_v1.demo.dto.req.UserChangePasswordReq;
import g_v1.demo.dto.req.UserReq;
import g_v1.demo.dto.req.UserUpdateReq;
import g_v1.demo.dto.res.UserRes;
import g_v1.demo.model.entity.User;

public interface UserService extends UserDetailsService {

    Page<UserRes> getAll(PageReq req);

    User getOne(String id);

    UserRes getById(String id);

    UserRes create(UserReq userReq);

    // UserRes reg(UserReq userReq);

    // User findByUsername(String username);

    void delete(String id);

    UserRes update(String id, UserUpdateReq req);

    // Page<UserRes> findAllUser(UserSearchRequest userSearchRequest);
    UserRes changePassword(String id, UserChangePasswordReq req);

    // UserRes getUserDetails(String id);
}
