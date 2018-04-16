package com.kuma2x.xauth.mapper;

import com.kuma2x.xauth.entity.SysRole;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper {

    @Select("select * from sys_role,sys_user_role where sys_user_role.rid = sys_role.id and sys_user_role.uid = #{uid}")
    @Results({
            @Result(property="name",column="name"),
            @Result(property="namezh",column="namezh"),
            @Result(property="menus",column="id",javaType=List.class,
                    many=@Many(select="com.kuma2x.xauth.mapper.SysMenuMapper.selectMenuByRid"))
    })
    List<SysRole> selectRoleByUId(int uid);

    List<SysRole> selectRole();
}