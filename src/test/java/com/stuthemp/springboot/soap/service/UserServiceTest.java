package com.stuthemp.springboot.soap.service;

import com.stuthemp.springboot.soap.generated.*;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.model.User;
import com.stuthemp.springboot.soap.repository.RoleRepository;
import com.stuthemp.springboot.soap.repository.UserRepository;
import com.stuthemp.springboot.soap.utils.RequestHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * todo Document type UserServiceTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RequestHandler requestHandler;

    private static List<Role> validRoles;

    private static List<User> existingUsers;

    private final String EXISTING_LOGIN = "user1";
    private final String NOT_EXISTING_LOGIN = "user3";

    private static ServiceStatus SUCCESS_STATUS;

    @BeforeClass
    public static void initData() {
        Role roleAdmin = new Role();
        roleAdmin.setName("Admin");
        roleAdmin.setId(1L);
        Role roleAnalyst = new Role();
        roleAnalyst.setName("Analyst");
        roleAnalyst.setId(2L);

        validRoles = Arrays.asList(roleAdmin, roleAnalyst);

        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("W1pow");
        user1.setName("Alex");
        user1.addRole(roleAdmin);
        User user2 = new User();
        user2.setLogin("user1");
        user2.setPassword("W1pow");
        user2.setName("Alex");
        user2.addRole(roleAnalyst);

        existingUsers = Arrays.asList(user1, user2);

        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setSuccess(true);

        SUCCESS_STATUS = serviceStatus;
    }
    @Test
    public void getUserByLoginReturnsExistingUser() {
        //Arrange
        GetUserByLoginRequest correctRequest = new GetUserByLoginRequest();
        correctRequest.setUserLogin(EXISTING_LOGIN);

        Mockito.doReturn(Optional.of(existingUsers.get(0)))
            .when(userRepository)
            .findUserByLogin(EXISTING_LOGIN);

        //Act
        GetUserByLoginResponse response = userService.getUserByLogin(correctRequest);

        //Assert
        Assert.assertEquals(EXISTING_LOGIN, response.getUser().getLogin());
    }

    @Test
    public void getUserByLoginReturnsFalseForNonExistingUser() {
        //Arrange
        GetUserByLoginRequest correctRequest = new GetUserByLoginRequest();
        correctRequest.setUserLogin(NOT_EXISTING_LOGIN);

        Mockito.doReturn(Optional.empty())
            .when(userRepository)
            .findUserByLogin(NOT_EXISTING_LOGIN);

        //Act
        GetUserByLoginResponse response = userService.getUserByLogin(correctRequest);

        //Assert
        Assert.assertFalse(response.getServiceStatus().isSuccess());
    }

//    @Test
//    public void addUserCreatesUserIfNotExists() {
//        AddUserRequest addUserRequest = new AddUserRequest();
//        UserInfo notExistingUser = new UserInfo();
//        notExistingUser.setLogin(NOT_EXISTING_LOGIN);
//        notExistingUser.setPassword("12Lkf");
//        notExistingUser.setName("Max");
//        RoleInfo roleInfo = new RoleInfo();
//        roleInfo.setName(validRoles.get(0).getName());
//        addUserRequest.setUser(notExistingUser);
//        addUserRequest.getRoles().add(roleInfo);
//
//        Mockito.doReturn(Optional.empty())
//            .when(userRepository)
//            .findUserByLogin(NOT_EXISTING_LOGIN);
//
//        Mockito.doReturn(SUCCESS_STATUS)
//            .when(requestHandler)
//            .validateUser(notExistingUser,addUserRequest.getRoles());
//
//        AddUserResponse response = userService.addUser(addUserRequest);
//
//        Assert.assertTrue(response.getServiceStatus().isSuccess());
//
//    }

    @Test
    public void addUserDoesNotCreateExistingUser() {
        AddUserRequest addUserRequest = new AddUserRequest();
        UserInfo notExistingUser = new UserInfo();
        notExistingUser.setLogin(EXISTING_LOGIN);
        notExistingUser.setPassword("12Lkf");
        notExistingUser.setName("Max");
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(validRoles.get(0).getName());
        addUserRequest.setUser(notExistingUser);
        addUserRequest.getRoles().add(roleInfo);

        Mockito.doReturn(Optional.of(existingUsers.get(0)))
            .when(userRepository)
            .findUserByLogin(EXISTING_LOGIN);

        Mockito.doReturn(SUCCESS_STATUS)
            .when(requestHandler)
            .validateUser(notExistingUser,addUserRequest.getRoles());

        AddUserResponse response = userService.addUser(addUserRequest);

        Assert.assertFalse(response.getServiceStatus().isSuccess());

    }

//    @Test
//    public void updateUserUpdatesExistingUser() {
//        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
//        UserInfo notExistingUser = new UserInfo();
//        notExistingUser.setLogin(EXISTING_LOGIN);
//        notExistingUser.setPassword("12Lkf");
//        notExistingUser.setName("Max");
//        RoleInfo roleInfo = new RoleInfo();
//        roleInfo.setName(validRoles.get(0).getName());
//        updateUserRequest.setUser(notExistingUser);
//        updateUserRequest.getRoles().add(roleInfo);
//
//        Mockito.doReturn(Optional.of(existingUsers.get(0)))
//            .when(userRepository)
//            .findUserByLogin(EXISTING_LOGIN);
//
//        Mockito.doReturn(SUCCESS_STATUS)
//            .when(requestHandler)
//            .validateUser(notExistingUser,updateUserRequest.getRoles());
//
//        UpdateUserResponse response = userService.updateUser(updateUserRequest);
//
//        Assert.assertTrue(response.getServiceStatus().isSuccess());
//    }

    @Test
    public void updateUserDoesNotUpdateNotExistingUser() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserInfo notExistingUser = new UserInfo();
        notExistingUser.setLogin(NOT_EXISTING_LOGIN);
        notExistingUser.setPassword("12Lkf");
        notExistingUser.setName("Max");
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(validRoles.get(0).getName());
        updateUserRequest.setUser(notExistingUser);
        updateUserRequest.getRoles().add(roleInfo);

        Mockito.doReturn(Optional.empty())
            .when(userRepository)
            .findUserByLogin(NOT_EXISTING_LOGIN);

        Mockito.doReturn(SUCCESS_STATUS)
            .when(requestHandler)
            .validateUser(notExistingUser,updateUserRequest.getRoles());

        UpdateUserResponse response = userService.updateUser(updateUserRequest);

        Assert.assertFalse("false", response.getServiceStatus().isSuccess());
    }

    @Test
    public void deleteUserDeletesExistingUser() {
        //Arrange
        DeleteUserRequest correctRequest = new DeleteUserRequest();
        correctRequest.setUserLogin(EXISTING_LOGIN);

        Mockito.doReturn(Optional.of(existingUsers.get(0)))
            .when(userRepository)
            .findUserByLogin(EXISTING_LOGIN);

        //Act
        DeleteUserResponse response = userService.deleteUser(correctRequest);

        //Assert
        Assert.assertTrue(response.getServiceStatus().isSuccess());
    }

    @Test
    public void deleteUserDoesNotDeleteNotExistingUser() {
        //Arrange
        DeleteUserRequest correctRequest = new DeleteUserRequest();
        correctRequest.setUserLogin(NOT_EXISTING_LOGIN);

        Mockito.doReturn(Optional.empty())
            .when(userRepository)
            .findUserByLogin(NOT_EXISTING_LOGIN);

        //Act
        DeleteUserResponse response = userService.deleteUser(correctRequest);

        //Assert
        Assert.assertFalse(response.getServiceStatus().isSuccess());
    }

    @Test
    public void getAllUsersTest() {

        Mockito.doReturn(existingUsers)
            .when(userRepository)
            .findAll();

        GetAllUsersResponse response = userService.getAllUsers();

        Assert.assertEquals(existingUsers.size(), response.getUsers().size());
    }

}
