package com.kuma2x.xauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.kuma2x.Xauth.mapper")
public class XauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(XauthApplication.class, args);
	}
}
