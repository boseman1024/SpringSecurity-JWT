package com.kuma2x.xauth.controller;

import com.kuma2x.xauth.serivce.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/auth/to")
    public String to(){
        return "to";
    }
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "admin";
    }

    @PostMapping("/auth/login")
    public Map<String,String> Login(HttpServletRequest request){
        String token = userService.login(request.getParameter("username"),request.getParameter("password"));
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        return tokenMap;
    }

}
