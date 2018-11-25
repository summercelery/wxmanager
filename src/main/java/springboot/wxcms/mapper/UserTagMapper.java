package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.UserTag;

import java.util.List;

public interface UserTagMapper {

    List<UserTag> getUserTagListByPage(UserTag searchEntity);



    @Select("select MAX(ID) from user_tag")
    Integer getMaxId();

    void addList(List<UserTag> list);


    /**
     * <p>
     * 删除（根据ID 批量删除）
     * </p>
     * @param ids 主键ID列表
     * @return int
     */
    Integer deleteBatchIds(String [] ids);

    int deleteByPrimaryKey(String id);

    int insert(UserTag record);

    int insertSelective(UserTag record);

    UserTag selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserTag record);

    int updateByPrimaryKey(UserTag record);

}