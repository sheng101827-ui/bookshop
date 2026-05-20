package com.daniel.controller;

import com.daniel.common.Result;
import com.daniel.common.ResultGenerator;
import com.daniel.pojo.User;
import com.daniel.service.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(UserController.class);

    @RequestMapping("")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/sessions",method = RequestMethod.POST)
    @ResponseBody
    public Result checkLogin(@RequestBody User user, HttpServletRequest request) {
        log.info("request: user/login , user: " + user.toString());
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getStudentid(), user.getPassword());
        try {
            currentUser.login(token);
            User authenticatedUser = (User) currentUser.getPrincipal();
            Map data = new HashMap();
            data.put("currentUser", authenticatedUser);
            return ResultGenerator.genSuccessResult(data);
        } catch (AuthenticationException e) {
            return ResultGenerator.genFailResult("学号或密码输入错误！");
        }
    }

    @RequestMapping(value = "/sessions",method = RequestMethod.DELETE)
    public Result logout(HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
        return ResultGenerator.genSuccessResult();
    }
}
