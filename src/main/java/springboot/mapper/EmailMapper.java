package springboot.mapper;


import org.apache.ibatis.annotations.Select;
import springboot.entity.Email;

public interface EmailMapper {

    @Select("select count(1) from user where email = #{email}")
    Integer countNumByEmail(String email);



    int deleteByPrimaryKey(String id);

    int insert(Email record);

    int insertSelective(Email record);

    Email selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Email record);

    int updateByPrimaryKey(Email record);
}