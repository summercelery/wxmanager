package springboot.wxcms.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.Role;

import java.util.List;


public interface RoleMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from role LEFT JOIN user_and_role ON role.id = user_and_role.role_id where user_id = #{userId}")
    List<Role> findRolesByUserId(String userId);

    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}