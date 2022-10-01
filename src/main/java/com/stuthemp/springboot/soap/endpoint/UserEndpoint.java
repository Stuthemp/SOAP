package com.stuthemp.springboot.soap.endpoint;

import com.stuthemp.springboot.soap.generated.*;
import com.stuthemp.springboot.soap.model.User;
import com.stuthemp.springboot.soap.service.UserService;
import com.stuthemp.springboot.soap.utils.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.persistence.EntityNotFoundException;


@Endpoint
public class UserEndpoint {

    private static final String NAMESPACE_URI = "http://generated.soap.springboot.stuthemp.com";

    private UserService userService;

    private RequestHandler handler;

    @Autowired
    public UserEndpoint(UserService userService, RequestHandler handler) {
        this.userService = userService;
        this.handler = handler;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        ServiceStatus serviceStatus = handler.validateUser(request.getUser(), request.getRoles());
        if(serviceStatus.isSuccess()){
            if(!userService.addUser(request.getUser(), request.getRoles())) {
                serviceStatus.setSuccess(false);
                serviceStatus.getErrors().add("User already exists");
            }
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        UpdateUserResponse response = new UpdateUserResponse();
        ServiceStatus serviceStatus = handler.validateUser(request.getUser(), request.getRoles());
        if(serviceStatus.isSuccess()){
            if(!userService.updateUser(request.getUser(),request.getRoles())){
                serviceStatus.setSuccess(false);
                serviceStatus.getErrors().add("User does not exist");
            }
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public GetUserByLoginResponse getUser(@RequestPayload GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try {
            User user = userService.getUserByLogin(request.getUserLogin());
            UserInfo userInfo = new UserInfo();
            userInfo.setName(user.getName());
            userInfo.setLogin(user.getLogin());
            userInfo.setPassword(user.getPassword());
            user.getRoles().forEach(role -> {
                RoleInfo roleInfo = new RoleInfo();
                roleInfo.setId(role.getId());
                response.getRoles().add(roleInfo);
            });
            response.setUser(userInfo);
            serviceStatus.setSuccess(true);
        } catch (EntityNotFoundException e) {
            serviceStatus.setSuccess(false);
            serviceStatus.getErrors().add(e.getMessage());
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setSuccess(userService.deleteUser(request.getUserLogin()));
        if(!serviceStatus.isSuccess()) serviceStatus.getErrors().add("User does not exist");
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers() {
        GetAllUsersResponse response = new GetAllUsersResponse();
        userService.getAllUsers().forEach(userInfo -> response.getUsers().add(userInfo));
        return response;
    }
}