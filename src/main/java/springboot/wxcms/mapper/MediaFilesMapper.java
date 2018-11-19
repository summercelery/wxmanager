package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.MediaFiles;

import java.util.List;

public interface MediaFilesMapper {


    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM media_files ORDER BY t.create_time desc ")
    List<MediaFiles> getMediaFileList();


    /**
     * 获取单条数据
     */
    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM media_files WHERE media_id = #{mediaId}")
    public MediaFiles getFileByMediaId(String mediaId);

    /**
     * 删除
     */
    @Delete("delete from media_files where media_id = #{mediaId}")
    void deleteByMediaId(String mediaId);

    /**
     * 条件查询
     */
    MediaFiles getFileBySou(MediaFiles entity);


    List<MediaFiles> getMediaListByPage(MediaFiles mediaFiles);

    int deleteByPrimaryKey(String id);

    int insert(MediaFiles record);

    int insertSelective(MediaFiles record);

    MediaFiles selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MediaFiles record);

    int updateByPrimaryKey(MediaFiles record);
}