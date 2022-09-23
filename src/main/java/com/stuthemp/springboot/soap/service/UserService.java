package com.stuthemp.springboot.soap.service;

import com.stuthemp.springboot.soap.generated.*;

public interface UserService {

    GetUserByLoginResponse getUserByLogin(GetUserByLoginRequest request);

    AddUserResponse addUser(AddUserRequest request);

    DeleteUserResponse deleteUser(DeleteUserRequest request);

    UpdateUserResponse updateUser(UpdateUserRequest request);

    GetAllUsersResponse getAllUsers();

}
