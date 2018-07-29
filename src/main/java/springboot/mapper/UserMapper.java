package springboot.mapper;

import org.apache.ibatis.annotations.*;
import springboot.entity.User;

@Mapper
public interface UserMapper{

    @ResultMap("BaseResultMap")
    @Select("select * from user where id = #{id}")
    User findUserById(String id);

    @ResultMap("BaseResultMap")
    @Select("select * from user where email = #{email}")
    User findUserByEmail(String email);

    @ResultMap("BaseResultMap")
    @Select("select * from user where phone = #{phone}")
    User findUserByPhone(String phone);

    @Select("select count(1) from user where phone = #{phone}")
    Integer countUserNumByPhone(String phone);


    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}
