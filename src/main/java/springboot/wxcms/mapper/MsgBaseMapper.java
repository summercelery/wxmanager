package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.entity.MsgNews;
import springboot.wxcms.entity.MsgText;

import java.util.List;

public interface MsgBaseMapper {


    @Update("UPDATE msg_base SET INPUT_CODE = #{inputcode} WHERE ID = #{id}")
    int updateInputcode(MsgBase msgBase);

    @ResultMap("BaseResultMap")
    @Select("SELECT t.* FROM msg_text t , msg_base b WHERE t.base_id = b.id AND b.input_code = #{inputcode}")
    MsgText getMsgTextByInputCode(String inputcode);

    @ResultMap("BaseResultMap")
    @Select("SELECT t.* FROM msg_text t , msg_base b WHERE t.base_id = b.id AND b.input_code = 'subscribe'")
    MsgText getMsgTextBySubscribe();

    @ResultMap("BaseResultMap")
    @Select("SELECT t.* FROM msg_text t , msg_base b WHERE t.base_id = b.id AND b.id = #{id}")
    MsgText getMsgTextByBaseId(String id);


    List<MsgNews> listMsgNewsByBaseId(String[] ids);



    List<MsgBase> listForPage(MsgBase searchEntity);

    int deleteByPrimaryKey(String id);

    int insert(MsgBase record);

    int insertSelective(MsgBase record);

    MsgBase selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgBase record);

    int updateByPrimaryKey(MsgBase record);
}