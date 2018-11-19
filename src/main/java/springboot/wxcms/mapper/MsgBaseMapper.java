package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Update;
import springboot.wxcms.entity.MsgBase;

import java.util.List;

public interface MsgBaseMapper {


    @Update("UPDATE msg_base SET INPUT_CODE = #{inputcode} WHERE ID = #{id}")
    int updateInputcode(MsgBase msgBase);


    List<MsgBase> listForPage(MsgBase searchEntity);

    int deleteByPrimaryKey(String id);

    int insert(MsgBase record);

    int insertSelective(MsgBase record);

    MsgBase selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgBase record);

    int updateByPrimaryKey(MsgBase record);
}