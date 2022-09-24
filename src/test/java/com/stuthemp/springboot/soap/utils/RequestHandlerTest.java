package com.stuthemp.springboot.soap.utils;

import com.stuthemp.springboot.soap.generated.RoleInfo;
import com.stuthemp.springboot.soap.generated.ServiceStatus;
import com.stuthemp.springboot.soap.generated.UserInfo;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.repository.RoleRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestHandlerTest {

    @Autowired
    RequestHandler requestHandler;
    @MockBean
    RoleRepository roleRepository;

    private final String CORRECT_PASSWORD = "pa55W0rd";
    private final String CORRECT_ROLE_NAME = "Admin";
    private final String INCORRECT_ROLE_NAME = "Teacher";
    private final String INCORRECT_PASSWORD = "bad password";
    private final String LOGIN = "login";
    private final String NAME = "name";
    private static List<Role> validRoles;

    @BeforeClass
    public static void initRoles() {
        Role roleAdmin = new Role();
        roleAdmin.setName("Admin");
        roleAdmin.setId(1L);
        Role roleAnalyst = new Role();
        roleAnalyst.setName("Analyst");
        roleAnalyst.setId(2L);

        validRoles = Arrays.asList(roleAdmin, roleAnalyst);
    }

    @Test
    public void requestWithoutRolesPassesValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName(NAME);
        userInfo.setLogin(LOGIN);
        userInfo.setPassword(CORRECT_PASSWORD);

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.emptyList());

        //Assert
        Assert.assertTrue(serviceStatus.isSuccess());
    }

    @Test
    public void requestWithCorrectCredentialsWithCorrectRolesPassesValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName(NAME);
        userInfo.setLogin(LOGIN);
        userInfo.setPassword(CORRECT_PASSWORD);

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(CORRECT_ROLE_NAME);

        Mockito.doReturn(validRoles)
            .when(roleRepository)
            .findAll();

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.singletonList(roleInfo));

        //Assert
        Assert.assertTrue(serviceStatus.isSuccess());
    }

    @Test
    public void requestWithIncorrectPasswordDoesNotPassValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName(NAME);
        userInfo.setLogin(LOGIN);
        userInfo.setPassword(INCORRECT_PASSWORD);

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(CORRECT_ROLE_NAME);

        Mockito.doReturn(validRoles)
            .when(roleRepository)
            .findAll();

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.singletonList(roleInfo));

        //Assert
        Assert.assertFalse(serviceStatus.isSuccess());
    }

    @Test
    public void requestWithNoLoginDoesNotPassValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName(NAME);
        userInfo.setLogin("");
        userInfo.setPassword(CORRECT_PASSWORD);

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(CORRECT_ROLE_NAME);

        Mockito.doReturn(validRoles)
            .when(roleRepository)
            .findAll();

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.singletonList(roleInfo));

        //Assert
        Assert.assertFalse(serviceStatus.isSuccess());
    }

    @Test
    public void requestWithNoNameDoesNotPassValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName("");
        userInfo.setLogin(LOGIN);
        userInfo.setPassword(CORRECT_PASSWORD);

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(CORRECT_ROLE_NAME);

        Mockito.doReturn(validRoles)
            .when(roleRepository)
            .findAll();

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.singletonList(roleInfo));

        //Assert
        Assert.assertFalse(serviceStatus.isSuccess());
    }

    @Test
    public void requestWithIncorrectRolesNotPassValidation() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setName(NAME);
        userInfo.setLogin(LOGIN);
        userInfo.setPassword(CORRECT_PASSWORD);

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(INCORRECT_ROLE_NAME);

        Mockito.doReturn(validRoles)
            .when(roleRepository)
            .findAll();

        //Act
        ServiceStatus serviceStatus = requestHandler.validateUser(userInfo, Collections.singletonList(roleInfo));

        //Assert
        Assert.assertFalse("false",serviceStatus.isSuccess());
    }
}
