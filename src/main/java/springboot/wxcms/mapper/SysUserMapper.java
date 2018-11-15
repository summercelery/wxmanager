package springboot.wxcms.mapper;

import org.apache.ibatis.annotations.*;
import springboot.wxcms.entity.SysUser;

public interface SysUserMapper{

    @ResultMap("BaseResultMap")
    @Select("select * from user where email = #{email}")
    SysUser findUserByEmail(String email);

    @ResultMap("BaseResultMap")
    @Select("select * from user where phone = #{phone}")
    SysUser findUserByPhone(String phone);

    @Select("select count(1) from user where phone = #{phone}")
    Integer countUserNumByPhone(String phone);


    @ResultMap("BaseResultMap")
    @Select("select * from sys_user where login_name = #{loginName}")
    SysUser findSysUserByLoginName(String loginName);

    @Select("select count(1) from sys_user where login_name = #{loginName}")
    Integer countUserNumByLoginName(String loginName);


    @Update("update sys_user set password = #{password} ,update_time = #{updateTime} WHERE id = #{id}")
    Integer updateLoginPwd(SysUser sysUser);


    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

}
