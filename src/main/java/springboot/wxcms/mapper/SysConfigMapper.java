package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import springboot.wxcms.entity.SysConfig;

import java.util.List;

public interface SysConfigMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from sys_config")
    List<SysConfig> getConfigList();


    @Select("select jvalue from sys_config WHERE  jkey = #{key}")
    String getValue(String key);

    @Select("select version()")
    String getMysqlVsesion();

    @Update("update sys_config set jvalue=#{value} where jkey = #{key}")
    int update(String key,String value);

    int deleteByPrimaryKey(String jkey);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(String jkey);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);
}