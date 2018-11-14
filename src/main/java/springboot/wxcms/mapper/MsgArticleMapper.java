package springboot.wxcms.mapper;


import springboot.wxcms.entity.MsgArticle;

public interface MsgArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgArticle record);

    int insertSelective(MsgArticle record);

    MsgArticle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgArticle record);

    int updateByPrimaryKey(MsgArticle record);
}