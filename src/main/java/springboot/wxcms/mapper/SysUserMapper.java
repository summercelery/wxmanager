package springboot.wxcms.mapper;

import org.apache.ibatis.annotations.*;
import springboot.wxcms.entity.SysUser;
import springboot.wxcms.entity.User;

public interface SysUserMapper{

    @ResultMap("BaseResultMap")
    @Select("select * from user where id = #{id}")
    SysUser findUserById(String id);

    @ResultMap("BaseResultMap")
    @Select("select * from user where email = #{email}")
    SysUser findUserByEmail(String email);

    @ResultMap("BaseResultMap")
    @Select("select * from user where phone = #{phone}")
    SysUser findUserByPhone(String phone);

    @Select("select count(1) from user where phone = #{phone}")
    Integer countUserNumByPhone(String phone);


    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

}
