package com.kuma2x.xauth.serivce.Impl;

import com.google.gson.Gson;
import com.kuma2x.xauth.entity.SysRole;
import com.kuma2x.xauth.entity.SysUser;
import com.kuma2x.xauth.mapper.SysUserMapper;
import com.kuma2x.xauth.serivce.UserService;
import com.kuma2x.xauth.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service()
public class UserServiceImpl implements UserService {

    //获取全局AuthenticationManagerBuilder
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Resource
    private JWTUtil jwtUtil;
    @Resource
    private PasswordEncoder passwordEncoder;

    /***
     * 继承UserDetailsService类重写loadUserByUsername
     * 根据用户名查找用户并返回
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("===loadUserByUsername===");
        SysUser user = sysUserMapper.selectByUsername(username);
        if(user == null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }
        System.out.println("user:"+new Gson().toJson(user));
        return new org.springframework.security.core.userdetails.User(user.getUsername()
                ,user.getPassword(),user.getEnabled()==1,
                true,true,true,mapToGrantedAuthorities(user.getRole()));
    }

    /**
     *获取用户所有权限集合
     * @param authorities
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<SysRole> authorities) {
        Set<GrantedAuthority> setAuths=new HashSet<>();
        for(SysRole userRole:authorities){
            /*
            for(SysMenu sysMenu : userRole.getMenus()){
                setAuths.add(new SimpleGrantedAuthority(sysMenu.getName()));
            }
            */
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }
        List<GrantedAuthority> result=new ArrayList<>(setAuths);
        return result;
    }

    /****
     * 登录，加密密码
     * 生成jwtToken并返回
     * @param user
     * @return
     */
    @Override
    public String login(String username,String password) {
        //password = passwordEncoder.encode(password);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        //绕开SS的Filter手动的完成验证并设置Authentication对象：
        //用户名和密码获得之后组合成 UsernamePasswordAuthenticationToken 的实例
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        if(authenticationManager==null){
            throw new AuthenticationServiceException("no authenticationManager defined");
        }
        //将该令牌传递给 AuthenticationManager 实例进行验证,验证成功后，AuthenticationManager 会返回填充好的 Authentication 实例
        Authentication authentication = authenticationManager.authenticate(upToken);
        //通过调用 SecurityContextHolder.getContext().setAuthentication(...) 建立安全上下文的实例，传递到返回的身份认证对象上
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //生成token并返回，多一次loadUserByUsername
        UserDetails userDetails = loadUserByUsername(username);
        //User user = sysUserMapper.selectByUsername(username);
        String token = jwtUtil.initToken(userDetails);
        return token;
    }

}
