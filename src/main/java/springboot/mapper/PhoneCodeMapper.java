package springboot.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.entity.PhoneCode;


public interface PhoneCodeMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from phone_code where phone = #{phone} and code = #{code} type = #{type}")
    PhoneCode findPhoneCodeByPhoneAndCodeAndType(@Param("phone") String phone,@Param("code")String code,@Param("type") String type);


    int deleteByPrimaryKey(String id);

    int insert(PhoneCode record);

    int insertSelective(PhoneCode record);

    PhoneCode selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PhoneCode record);

    int updateByPrimaryKey(PhoneCode record);
}