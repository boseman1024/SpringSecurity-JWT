package com.kuma2x.xauth.serivce;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    public String login(String username,String password);
}
