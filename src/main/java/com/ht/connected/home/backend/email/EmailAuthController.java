package com.ht.connected.home.backend.email;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserActive;
import com.ht.connected.home.backend.client.user.UserService;
import com.ht.connected.home.backend.common.AuditLogger;
import com.ht.connected.home.backend.controller.rest.CommonController;

@Controller
public class EmailAuthController extends CommonController {

    UserService usersService;

    @Autowired
    public EmailAuthController(UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/adduser")
    public ResponseEntity<String> addAuthUser(@RequestParam(value = "user_email", required = true) String userEmail,
            @RequestParam(value = "redirected_code", required = true) String redirected_code) throws IOException {
    	AuditLogger.startLog(EmailAuthController.class, "Attempt Email certification : " + userEmail);
        User rtnUsers = usersService.getUser(userEmail);
        if (null != rtnUsers) {
            if (redirected_code.equals(rtnUsers.getRedirectiedCode())) {
                rtnUsers.setActive(UserActive.EMAIL_AUTH.ordinal());
                usersService.modify(rtnUsers.getNo(), rtnUsers);
            }
            InputStream file =getClass().getClassLoader().getResourceAsStream("templates/email/userRegisterConfirm.html");
            AuditLogger.endLog(EmailAuthController.class, "Attempt Email certification : Succeed");
            return new ResponseEntity(IOUtils.toString(file, "UTF-8").replace("{{user.email}}", userEmail), HttpStatus.OK);
        }
        AuditLogger.endLog(EmailAuthController.class, "Attempt Email certification : failed (Invalid parameter)");
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
