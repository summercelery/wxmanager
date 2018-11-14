package springboot.wxcms.mapper;


import springboot.wxcms.entity.MsgText;

public interface MsgTextMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgText record);

    int insertSelective(MsgText record);

    MsgText selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgText record);

    int updateByPrimaryKey(MsgText record);
}