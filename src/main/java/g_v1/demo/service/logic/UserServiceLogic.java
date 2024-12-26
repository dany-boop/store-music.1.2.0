package g_v1.demo.service.logic;

import java.util.Optional;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import g_v1.demo.constant.Constant;
import g_v1.demo.dto.req.PageReq;
import g_v1.demo.dto.req.UserChangePasswordReq;
import g_v1.demo.dto.req.UserReq;
import g_v1.demo.dto.req.UserUpdateReq;
import g_v1.demo.dto.res.UserRes;
import g_v1.demo.model.entity.User;
import g_v1.demo.model.enums.UserRole;
import g_v1.demo.repository.UserRepository;
import g_v1.demo.service.RoleService;
import g_v1.demo.service.UserService;
import g_v1.demo.util.LogUtil;
import g_v1.demo.util.SortUtil;
import g_v1.demo.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceLogic implements UserService {

    private final RoleService roleService;
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    // @Value("admin1")
    // private String ADMIN_USERNAME;

    // @Value("admin1")
    // private String ADMIN_PASSWORD;

    @Value("${app.store-music.admin-username}")
    private String ADMIN_USERNAME;

    @Value("${app.store-music.admin-password}")
    private String ADMIN_PASSWORD;

    @PostConstruct
    public void init() {
        if (userRepository.existsByUsername(ADMIN_USERNAME))
            return;
        User user = User.builder()
                .username(ADMIN_USERNAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(roleService.getOne("ROLE_ADMIN"))
                .build();
        userRepository.save(user);
    }

    @Override
    public Page<UserRes> getAll(PageReq req) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedFilter");
        filter.setParameter("isDeleted", req.isDeleted());
        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                SortUtil.parseSort(req.getSort()));
        Page<User> users = userRepository.findAll(pageable);
        session.disableFilter("deletedFilter");
        return users.map(user -> UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build());
    }

    @Override
    public UserRes getById(String id) {
        User user = getOne(id); // Assuming getOne(id) retrieves the User entity
        return UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public User getOne(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_GET_USER_MSG));
    }

    @Override
    public UserRes create(UserReq req) {
        validationUtil.validate(req);
        UserRole userRole = req.getRole();

        if (userRole == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role cannot be null");
        }

        // Build and save the user
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(userRole) // Use the enum directly
                .build();
        userRepository.saveAndFlush(user);
        return UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getDescription())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserRes changePassword(String id, UserChangePasswordReq req) {
        LogUtil.info("changing password");
        validationUtil.validate(req);
        User user = getOne(id);
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_VERIFY_PASSWORD_MSG);
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.saveAndFlush(user);
        LogUtil.info("finished changing password");
        return UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserRes update(String id, UserUpdateReq req) {
        LogUtil.info("updating user");
        validationUtil.validate(req);
        UserRole role = roleService.getOne(req.getRoleId());
        User user = getOne(id);
        user.setUsername(req.getUsername());
        user.setRole(role);
        userRepository.saveAndFlush(user);
        LogUtil.info("finished updating user");
        return UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public void delete(String id) {
        LogUtil.info("deleting user");
        User user = getOne(id);
        user.setDeleted(!user.isDeleted());
        userRepository.save(user);
        LogUtil.info("finished deleting user");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_USER_HAS_BEEN_DELETED_MSG);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_GET_USER_MSG);
        return user.get();
    }
}
