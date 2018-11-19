package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.TplMsgText;

import java.util.List;

public interface TplMsgTextMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from tpl_msg_text where base_id = #{baseId}")
    TplMsgText getByBaseId(String baseId);

    List<TplMsgText> getTplMsgTextByPage(TplMsgText searchEntity);

    List<TplMsgText> getTplMsgTextList(TplMsgText searchEntity);

    int deleteByPrimaryKey(String id);

    int insert(TplMsgText record);

    int insertSelective(TplMsgText record);

    TplMsgText selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TplMsgText record);

    int updateByPrimaryKey(TplMsgText record);
}