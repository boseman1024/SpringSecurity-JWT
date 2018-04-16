package com.kuma2x.xauth.config;

import com.kuma2x.xauth.filter.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.exceptUrl}")
    private String exceptUrl;

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public JWTAuthenticationFilter authenticationTokenFilter() throws Exception {
        return new JWTAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 源码：
        // ((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();
        http.cors().and().csrf().disable()
            //禁用session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                //允许匿名访问
                .antMatchers(exceptUrl).permitAll()
                //除上以外所有请求均需授权访问
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationTokenFilter(),UsernamePasswordAuthenticationFilter.class);

        //禁用缓存
        http.headers().cacheControl().disable();
    }

    //AuthenticationManagerBuilder 通过 @Autowired 注解被注入到一个 @Bean 中的方法，这会导致它构造的是全局的AuthenticationManager
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //设置userDetailsService
                .userDetailsService(userDetailsService)
                //加载密码编码器
                .passwordEncoder(passwordEncoder());
    }
/*
    //使用 @Override 注解会导致 AuthenticationManagerBuilder 构建的是一个局部的AuthenticationManager
    @Override
    public configure(AuthenticationManagerBuilder builder) {
        auth.jdbcAuthentication().dataSource(dataSource).withUser("dave")
                .password("secret").roles("USER");
    }

*/

    //装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
