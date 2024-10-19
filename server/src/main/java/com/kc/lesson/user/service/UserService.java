package com.kc.lesson.user.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.user.entity.User;

public interface UserService {
    PageInfo<User> getUserList(String keyword, String role, int page, int pageSize);

    User getAdminUser(User user);

    User getNormalUser(User user);

    void createUser(User user);

    void deleteUser(String id);

    void updateUser(User user);

    User getUserByToken(String token);

    User getUserByUserName(String username);

    User getUserDetail(String userId);
}
