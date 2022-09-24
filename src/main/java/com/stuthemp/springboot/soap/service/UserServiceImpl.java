package com.stuthemp.springboot.soap.service;

import com.stuthemp.springboot.soap.generated.*;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.model.User;
import com.stuthemp.springboot.soap.repository.RoleRepository;
import com.stuthemp.springboot.soap.repository.UserRepository;
import com.stuthemp.springboot.soap.utils.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service класс, работающий с типом данных User
 */
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RequestHandler requestHandler;

    RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RequestHandler requestHandler, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.requestHandler = requestHandler;
        this.roleRepository = roleRepository;
    }

    @Override
    public GetUserByLoginResponse getUserByLogin(GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        String login = request.getUserLogin();

        Optional<User> optionalUser = userRepository.findUserByLogin(login);

        if(optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            UserInfo userInfo = new UserInfo();

            Set<Role> roles = existingUser.getRoles();

            userInfo.setLogin(existingUser.getLogin());
            userInfo.setName(existingUser.getName());
            userInfo.setPassword(existingUser.getPassword());

            for (Role role: roles) {
                RoleInfo roleInfo = new RoleInfo();
                roleInfo.setName(role.getName());
                response.getRoles().add(roleInfo);
            }

            response.setUser(userInfo);
            return response;

        } else {
            serviceStatus.setSuccess(false);
            serviceStatus.getErrors().add("EntityNotFoundException");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }

    @Override
    public AddUserResponse addUser(AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        UserInfo requestUser = request.getUser();
        List<RoleInfo> roleInfoList = request.getRoles();

        ServiceStatus serviceStatus = requestHandler.validateUser(requestUser, roleInfoList);

        if(userExists(requestUser.getLogin())) {
            serviceStatus.setSuccess(false);
            serviceStatus.getErrors().add("User already exist");
        }

        if(!(serviceStatus.getErrors().isEmpty())) {
            response.setServiceStatus(serviceStatus);
            return response;
        } else {

            User user = new User();

            for (RoleInfo roleInfo: roleInfoList) {
                Role role = roleRepository.findByName(roleInfo.getName());
                user.addRole(role);
            }

            user.setLogin(requestUser.getLogin());
            user.setName(requestUser.getName());
            user.setPassword(requestUser.getPassword());
            userRepository.save(user);

            serviceStatus.setSuccess(true);
            response.setServiceStatus(serviceStatus);
            return response;
        }

    }

    @Override
    public DeleteUserResponse deleteUser(DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        String login = request.getUserLogin();

        Optional<User> optionalUser = userRepository.findUserByLogin(login);
        if(optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            serviceStatus.setSuccess(true);
            response.setServiceStatus(serviceStatus);
            return response;
        }

        serviceStatus.setSuccess(false);
        serviceStatus.getErrors().add("User with provided login was not found");
        response.setServiceStatus(serviceStatus);

        return response;
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        User newUser = new User();
        UpdateUserResponse response = new UpdateUserResponse();
        UserInfo requestUser = request.getUser();
        List<RoleInfo> roleInfoList = request.getRoles();
        ServiceStatus status = requestHandler.validateUser(requestUser, roleInfoList);

        if(!(status.getErrors().isEmpty())) {
            response.setServiceStatus(status);
            return response;
        }

        if(userExists(requestUser.getLogin())) {
            newUser.setLogin(requestUser.getLogin());
            newUser.setName(requestUser.getName());
            newUser.setPassword(requestUser.getPassword());

            for (RoleInfo roleInfo: roleInfoList) {
                Role role = roleRepository.findByName(roleInfo.getName());
                newUser.addRole(role);
            }

            userRepository.save(newUser);
            status.setSuccess(true);
            response.setServiceStatus(status);
            return response;
        }
        status.setSuccess(false);
        status.getErrors().add("User Not found");
        response.setServiceStatus(status);
        return response;
    }

    @Override
    public GetAllUsersResponse getAllUsers() {
        GetAllUsersResponse response = new GetAllUsersResponse();

        List<User> users = userRepository.findAll();
        List<UserInfo> responseBody = users.stream().map(user -> {
            UserInfo responseUser  = new UserInfo();
            responseUser.setLogin(user.getLogin());
            responseUser.setName(user.getName());
            responseUser.setPassword(user.getPassword());
            return responseUser;
        }).collect(Collectors.toList());

        for (UserInfo userInfo: responseBody) {
            response.getUsers().add(userInfo);
        }

        return response;
    }

    private boolean userExists(String login) {
        Optional<User> optionalUser = userRepository.findUserByLogin(login);

        return optionalUser.isPresent();
    }
}