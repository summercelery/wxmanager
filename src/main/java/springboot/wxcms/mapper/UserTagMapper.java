package springboot.wxcms.mapper;


import springboot.wxcms.entity.UserTag;

public interface UserTagMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserTag record);

    int insertSelective(UserTag record);

    UserTag selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserTag record);

    int updateByPrimaryKey(UserTag record);
}