package com.kc.lesson.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.user.entity.User;
import com.kc.lesson.user.mapper.UserMapper;
import com.kc.lesson.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public PageInfo<User> getUserList(String keyword, String role, int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<User> list = userMapper.getUserList(keyword, role);
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public User getAdminUser(User user) {
        return userMapper.getAdminUser(user.getUsername(), user.getPassword());
    }

    @Override
    public User getNormalUser(User user) {
        return userMapper.getNormalUser(user.getUsername(), user.getPassword());
    }

    @Override
    public void createUser(User user) {
        user.setAvatar("avatar.jpg");
        userMapper.insert(user);
    }

    @Override
    public void deleteUser(String id) {
        userMapper.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateSelective(user);
    }

    @Override
    public User getUserByToken(String token) {
        return userMapper.getUserByToken(token);
    }

    @Override
    public User getUserByUserName(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override
    public User getUserDetail(String userId) {
        return userMapper.getUserDetail(userId);
    }
}
