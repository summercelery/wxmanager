package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import springboot.wxcms.entity.MsgNews;

import java.util.List;

public interface MsgNewsMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from msg_news where media_id = #{mediaId}")
    List<MsgNews> getByMediaId(String mediaId);

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM msg_news t , msg_base b WHERE t.base_id = b.id ORDER BY t.ID")
    List<MsgNews> listForPage(MsgNews searchEntity);

    @Update("UPDATE msg_news SET URL = #{url} WHERE id = #{id}")
    void updateUrl(MsgNews entity);

    @ResultMap("BaseResultMap")
    @Select("SELECT t.* from msg_news t , msg_base b WHERE t.base_id = b.id ORDER BY RAND() LIMIT #{num}")
    List<MsgNews> getRandomMsg(Integer num);

    @ResultMap("BaseResultMap")
    @Select("SELECT t.* from msg_news t ,msg_base b WHERE b.input_code like CONCAT('%','${inputcode}','%' ) and t.base_id = b.id ORDER BY RAND() LIMIT #{num}")
    List<MsgNews> getRandomMsgByContent(String inputcode ,Integer num);


    List<MsgNews> getMsgNewsByIds(String[] array);

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM msg_news WHERE base_id = #{baseid}")
    MsgNews getByBaseId(String baseid);

    /**
     * 同步到微信后更新表
     */
    @Update("UPDATE msg_news SET media_id = #{mediaId} WHERE id = #{id}")
    public void updateMediaId(MsgNews entity);


    /**
     * 查询图文列表
     */
    @ResultMap("BaseResultMap")
    @Select("select * from msg_news order by create_time")
    List<MsgNews> getMsgNewsList();

    /**
     * 删除
     */
    @Delete("DELETE FROM msg_news WHERE media_id = #{mediaId}")
    void deleteByMediaId(String mediaId);


    List<MsgNews> getWebNewsListByPage(MsgNews searchEntity);



    int deleteByPrimaryKey(String id);

    int insert(MsgNews record);

    int insertSelective(MsgNews record);

    MsgNews selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgNews record);

    int updateByPrimaryKey(MsgNews record);
}