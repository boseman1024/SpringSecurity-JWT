package com.kuma2x.xauth.mapper;

import com.kuma2x.xauth.entity.SysUser;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper {

    @Select("select * from sys_user where username = #{username}")
    @Results({
            @Result(property="username",column="username"),
            @Result(property="password",column="password"),
            @Result(property="name",column="name"),
            @Result(property="phone",column="phone"),
            @Result(property="address",column="address"),
            @Result(property="enabled",column="enabled"),
            @Result(property="role",column="id",javaType=List.class,
                    many=@Many(select="com.kuma2x.xauth.mapper.SysRoleMapper.selectRoleByUId"))
    })
    SysUser selectByUsername(String username);



}