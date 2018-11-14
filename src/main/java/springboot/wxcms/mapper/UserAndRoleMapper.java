package springboot.wxcms.mapper;


import springboot.wxcms.entity.UserAndRole;

public interface UserAndRoleMapper {
    int deleteByPrimaryKey(UserAndRole key);

    int insert(UserAndRole record);

    int insertSelective(UserAndRole record);
}