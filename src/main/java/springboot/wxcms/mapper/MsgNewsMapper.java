package springboot.wxcms.mapper;


import springboot.wxcms.entity.MsgNews;

public interface MsgNewsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgNews record);

    int insertSelective(MsgNews record);

    MsgNews selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgNews record);

    int updateByPrimaryKey(MsgNews record);
}