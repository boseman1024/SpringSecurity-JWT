package com.kuma2x.xauth.mapper;

import com.kuma2x.xauth.entity.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper {

    @Select("select * from sys_menu,sys_role_menu where sys_menu.id = sys_role_menu.mid and sys_role_menu.rid = #{rid}")
    List<SysMenu> selectMenuByRid(int rid);
}