package com.stuthemp.springboot.soap.service;

import com.stuthemp.springboot.soap.generated.*;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.model.User;

import java.util.List;

public interface UserService {

    User getUserByLogin(String login);

    boolean addUser(UserInfo userInfo, List<RoleInfo> roles);

    boolean deleteUser(String login);

    boolean updateUser(UserInfo userInfo, List<RoleInfo> roles);

    List<UserInfo> getAllUsers();

}
