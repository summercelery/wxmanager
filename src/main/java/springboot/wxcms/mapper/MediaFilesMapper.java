package springboot.wxcms.mapper;


import springboot.wxcms.entity.MediaFiles;

public interface MediaFilesMapper {
    int deleteByPrimaryKey(String id);

    int insert(MediaFiles record);

    int insertSelective(MediaFiles record);

    MediaFiles selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MediaFiles record);

    int updateByPrimaryKey(MediaFiles record);
}