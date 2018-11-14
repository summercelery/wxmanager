package springboot.wxcms.mapper;


import springboot.wxcms.entity.TplMsgText;

public interface TplMsgTextMapper {
    int deleteByPrimaryKey(String id);

    int insert(TplMsgText record);

    int insertSelective(TplMsgText record);

    TplMsgText selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TplMsgText record);

    int updateByPrimaryKey(TplMsgText record);
}