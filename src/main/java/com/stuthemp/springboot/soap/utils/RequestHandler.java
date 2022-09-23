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
        StringBuilder errors = new StringBuilder("");

        String login = userInfo.getLogin();
        String name = userInfo.getName();
        String password = userInfo.getPassword();

        if(login.isEmpty())
            errors.append("Login not provided, ");
        if(name.isEmpty())
            errors.append("Name not provided, ");
        if(password.isEmpty())
            errors.append("Password not provided, ");
        else  {
            if (!isPasswordValid(password)) {
                errors.append("Password does not match pattern, ");
            }

        }

        List<String> existingRoles = roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());

        for (RoleInfo providedRole: roleInfoList) {
            if (!existingRoles.contains(providedRole.getName())) {
                errors.append("Role ").append(providedRole.getName()).append(" does not exist, ");
            }
        }

        if (!errors.toString().isEmpty()){
            serviceStatus.setStatus("false");
            String errorString = errors.toString();
            serviceStatus.setErrors(errorString.substring(0,errors.lastIndexOf(", ")));
            return serviceStatus;
        }

        serviceStatus.setStatus("true");
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
