package springboot.wxcms.mapper;


import springboot.wxcms.entity.MsgBase;

public interface MsgBaseMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgBase record);

    int insertSelective(MsgBase record);

    MsgBase selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgBase record);

    int updateByPrimaryKey(MsgBase record);
}