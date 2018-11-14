package springboot.wxcms.mapper;


import springboot.wxcms.entity.ImgResource;

public interface ImgResourceMapper {
    int deleteByPrimaryKey(String id);

    int insert(ImgResource record);

    int insertSelective(ImgResource record);

    ImgResource selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ImgResource record);

    int updateByPrimaryKey(ImgResource record);
}