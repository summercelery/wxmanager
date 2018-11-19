package springboot.wxcms.mapper;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.MsgText;

import java.util.List;

public interface MsgTextMapper {

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM msg_text WHERE base_id = #{baseid}")
    MsgText getByBaseId(String baseid);


    @ResultMap("BaseResultMap")
    @Select("SELECT t.* from msg_text t , msg_base b WHERE b.input_code = #{inputCode} and t.base_id = b.id ORDER BY RAND() LIMIT 1")
    MsgText getRandomMsg(String inputCode);


    @ResultMap("BaseResultMap")
    @Select("SELECT t.* from msg_text t , msg_base b WHERE t.base_id = b.id ORDER BY RAND() LIMIT 1")
    MsgText getRandomMsg2();


    List<MsgText> getMsgTextByPage(MsgText searchEntity);

    List<MsgText> getMsgTextList(MsgText searchEntity);


    int deleteByPrimaryKey(String id);

    int insert(MsgText record);

    int insertSelective(MsgText record);

    MsgText selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgText record);

    int updateByPrimaryKey(MsgText record);
}