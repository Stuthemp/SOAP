package com.stuthemp.springboot.soap.utils;

import com.stuthemp.springboot.soap.generated.RoleInfo;
import com.stuthemp.springboot.soap.generated.ServiceStatus;
import com.stuthemp.springboot.soap.generated.UserInfo;
import com.stuthemp.springboot.soap.model.Role;
import com.stuthemp.springboot.soap.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс отвечающий за поиск ошибок в запросе.
 * Создавая, думал что понадобится больше методов, поэтому и создал отделльный класс.
 * В данной реализации разумнее вынести его единственный метод в private метод UserServiceImpl,
 * но если бы приложение расширялось, я бы оставил отдельным классом.
 */
@Component
public class RequestHandler {

    RoleRepository roleRepository;

    @Autowired
    public RequestHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ServiceStatus validateUser(UserInfo userInfo, List<RoleInfo> roleInfoList) {
        ServiceStatus serviceStatus = new ServiceStatus();

        String login = userInfo.getLogin();
        String name = userInfo.getName();
        String password = userInfo.getPassword();

        if(login.isEmpty())
            serviceStatus.getErrors().add("Login not provided");
        if(name.isEmpty())
            serviceStatus.getErrors().add("Name not provided");
        if(password.isEmpty())
            serviceStatus.getErrors().add("Password not provided");
        else  {
            if (!isPasswordValid(password)) {
                serviceStatus.getErrors().add("Password does not match pattern");
            }

        }

        List<String> existingRoles = roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());

        for (RoleInfo providedRole: roleInfoList) {
            if (!existingRoles.contains(providedRole.getName())) {
                serviceStatus.getErrors().add("Role "+ providedRole.getName() + " does not exist");
            }
        }

        if (!serviceStatus.getErrors().isEmpty()){
            serviceStatus.setSuccess(false);
            return serviceStatus;
        }

        serviceStatus.setSuccess(true);
        return serviceStatus;
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "(?=.*\\d)(?=.*[A-Z])[\\da-zA-Z]";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        int counter = 0;
        while (matcher.find()) {
            counter++;
        }
        return counter >= 2;
    }
}
