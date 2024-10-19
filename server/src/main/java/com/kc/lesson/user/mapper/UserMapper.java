package com.kc.lesson.user.mapper;

import com.kc.lesson.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getUserList(String keyword,String role);

    User getAdminUser(@Param("username") String username, @Param("password") String password);

    User getNormalUser(@Param("username") String username, @Param("password") String password);

    void insert(User user);

    void deleteById(String id);

    void updateById(User user);

    User getUserByToken(String token);

    User getUserByUserName(String username);

    User getUserDetail(String userId);

    int updateSelective(User user);
}
