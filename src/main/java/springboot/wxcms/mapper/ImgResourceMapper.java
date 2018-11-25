package springboot.wxcms.mapper;


import springboot.wxcms.entity.ImgResource;

import java.util.List;

public interface ImgResourceMapper {

    List<ImgResource> getImgListByPage(ImgResource entity);

    int deleteByPrimaryKey(String id);

    int insert(ImgResource record);

    int insertSelective(ImgResource record);

    ImgResource selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ImgResource record);

    int updateByPrimaryKey(ImgResource record);
}