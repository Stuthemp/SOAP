package com.stuthemp.springboot.soap.service;

import com.stuthemp.springboot.soap.generated.*;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.model.User;
import com.stuthemp.springboot.soap.repository.RoleRepository;
import com.stuthemp.springboot.soap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service класс, работающий с типом данных User
 */
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User getUserByLogin(String login) {
        if(!userExists(login))
            throw new EntityNotFoundException("User does not exist");
        return userRepository.findUserByLogin(login).orElseThrow(() -> new EntityNotFoundException("Handler not work correctly"));
    }
    @Override
    public boolean addUser(UserInfo userInfo, List<RoleInfo> roles) {

        if(userExists(userInfo.getLogin())) {
            return false;
        }

        User user = new User();
        for (RoleInfo roleInfo: roles) {
            Role role = roleRepository.findById(roleInfo.getId())
                .orElseThrow(() -> new EntityNotFoundException("Handler not work correctly"));;
            user.addRole(role);
        }
        user.setLogin(userInfo.getLogin());
        user.setName(userInfo.getName());
        user.setPassword(userInfo.getPassword());
        userRepository.save(user);

        return true;
    }
    @Override
    public boolean deleteUser(String login) {
        if(!userExists(login)) return false;
        userRepository.delete(userRepository.findUserByLogin(login)
            .orElseThrow(() -> new EntityNotFoundException("Handler not work correctly")));
        return true;
    }
    @Override
    public boolean updateUser(UserInfo userInfo, List<RoleInfo> roles) {
        User newUser = new User();

        if(!userExists(userInfo.getLogin()))
            return false;

        newUser.setLogin(userInfo.getLogin());
        newUser.setName(userInfo.getName());
        newUser.setPassword(userInfo.getPassword());
        roles.forEach(roleInfo -> newUser.addRole(roleRepository.findById(roleInfo.getId())
            .orElseThrow(() -> new EntityNotFoundException("Handler not work correctly"))));

        userRepository.save(newUser);
        return true;
    }
    @Override
    public List<UserInfo> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserInfo responseUser  = new UserInfo();
            responseUser.setLogin(user.getLogin());
            responseUser.setName(user.getName());
            responseUser.setPassword(user.getPassword());
            return responseUser;
        }).collect(Collectors.toList());
    }

    private boolean userExists(String login) {
        Optional<User> optionalUser = userRepository.findUserByLogin(login);

        return optionalUser.isPresent();
    }
}