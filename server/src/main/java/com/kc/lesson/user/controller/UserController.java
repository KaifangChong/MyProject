package com.kc.lesson.user.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.user.entity.User;
import com.kc.lesson.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String SALT = "abcd1234";

    @Autowired
    UserService userService;

    @Value("${File.uploadPath}")
    private String uploadPath;

    // 查询列表
    @GetMapping("/list")
    public KCResponse list(String keyword, String role,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        PageInfo<User> list = userService.getUserList(keyword, role, page, pageSize);
        return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
    }

    // 查询详情
    @GetMapping("/detail")
    public KCResponse detail(String userId) {
        User user = userService.getUserDetail(userId);
        return new KCResponse(ResponseCode.SUCCESS, "查询成功", user);
    }

    // 后台用户登录
    @PostMapping("/login")
    public KCResponse login(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        User responseUser = userService.getAdminUser(user);
        if (responseUser != null) {
            return new KCResponse(ResponseCode.SUCCESS, "登录成功", responseUser);
        } else {
            return new KCResponse(ResponseCode.FAIL, "用户名或密码错误");
        }
    }

    // 普通用户登录
    @PostMapping("/userLogin")
    public KCResponse userLogin(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        User responseUser = userService.getNormalUser(user);
        if (responseUser != null) {
            return new KCResponse(ResponseCode.SUCCESS, "登录成功", responseUser);
        } else {
            return new KCResponse(ResponseCode.FAIL, "用户名或密码错误");
        }
    }

    // 普通用户注册
    @PostMapping("/userRegister")
    @Transactional
    public KCResponse userRegister(User user) throws IOException {
        if (validateUserForRegistration(user)) {
            user.setPassword(hashPassword(user.getPassword()));
            user.setToken(hashUsername(user.getUsername()));
            user.avatar = saveAvatar(user);
            user.setRole(String.valueOf(User.NormalUser));
            user.setStatus("0");
            user.setCreateTime(String.valueOf(System.currentTimeMillis()));

            userService.createUser(user);
            return new KCResponse(ResponseCode.SUCCESS, "注册成功");
        }
        return new KCResponse(ResponseCode.FAIL, "注册失败");
    }

    // 添加用户
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/create")
    @Transactional
    public KCResponse create(User user) throws IOException {
        if (!StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getPassword())) {
            if (userService.getUserByUserName(user.getUsername()) != null) {
                return new KCResponse(ResponseCode.FAIL, "用户名重复");
            }

            user.setPassword(hashPassword(user.getPassword()));
            user.setToken(hashUsername(user.getUsername()));
            user.setCreateTime(String.valueOf(System.currentTimeMillis()));
            user.avatar = saveAvatar(user);

            userService.createUser(user);
            return new KCResponse(ResponseCode.SUCCESS, "用户创建成功");
        }
        return new KCResponse(ResponseCode.FAIL, "创建失败");
    }

    // 删除用户
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                userService.deleteUser(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新用户
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(User user) throws IOException {
        user.setPassword(null); // 不允许直接更新密码
        user.avatar = saveAvatar(user);
        userService.updateUser(user);
        return new KCResponse(ResponseCode.SUCCESS, "更新成功");
    }

    // 普通用户更新信息
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/updateUserInfo")
    @Transactional
    public KCResponse updateUserInfo(User user) throws IOException {
        User tmpUser = userService.getUserDetail(user.getId() + "");
        if (isNormalUser(tmpUser)) {
            user.setUsername(null); // 用户名不能修改
            user.setPassword(null); // 密码不能修改
            user.setRole(String.valueOf(User.NormalUser));
            user.avatar = saveAvatar(user);
            userService.updateUser(user);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        }
        return new KCResponse(ResponseCode.FAIL, "非法操作");
    }

    // 修改密码
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/updatePwd")
    @Transactional
    public KCResponse updatePwd(String userId, String password, String newPassword) throws IOException {
        User user = userService.getUserDetail(userId);
        if (isNormalUser(user)) {
            if (user.getPassword().equals(hashPassword(password))) {
                user.setPassword(hashPassword(newPassword));
                userService.updateUser(user);
                return new KCResponse(ResponseCode.SUCCESS, "密码更新成功");
            } else {
                return new KCResponse(ResponseCode.FAIL, "原密码错误");
            }
        }
        return new KCResponse(ResponseCode.FAIL, "非法操作");
    }

    // 保存头像文件
    private String saveAvatar(User user) throws IOException {
        MultipartFile file = user.getAvatarFile();
        if (file != null && !file.isEmpty()) {
            String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filePath = uploadPath + File.separator + "avatar" + File.separator + newFileName;
            File destFile = new File(filePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            file.transferTo(destFile);
            return newFileName;
        }
        return null;
    }

    // 校验用户注册信息
    private boolean validateUserForRegistration(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getRePassword())) {
            return false;
        }
        if (userService.getUserByUserName(user.getUsername()) != null) {
            return false;
        }
        if (!user.getPassword().equals(user.getRePassword())) {
            return false;
        }
        return true;
    }

    // 哈希密码
    private String hashPassword(String password) {
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }

    // 哈希用户名生成 token
    private String hashUsername(String username) {
        return DigestUtils.md5DigestAsHex((username + SALT).getBytes());
    }

    // 检查是否为普通用户
    private boolean isNormalUser(User user) {
        return user.getRole().equals(String.valueOf(User.NormalUser));
    }
}
