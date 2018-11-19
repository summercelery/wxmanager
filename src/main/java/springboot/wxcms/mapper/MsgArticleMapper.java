package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.MsgArticle;

import java.util.List;

public interface MsgArticleMapper {

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM msg_article WHERE NEWS_ID = #{newsId}")
    List<MsgArticle> getByNewsId(String newsId);


    void insertByBatch(List<MsgArticle> articles);


    @Delete("DELETE FROM msg_article WHERE NEWS_ID = #{id}")
    void deleteByBatch(String id);



    int deleteByPrimaryKey(String id);

    int insert(MsgArticle record);

    int insertSelective(MsgArticle record);

    MsgArticle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgArticle record);

    int updateByPrimaryKey(MsgArticle record);
}