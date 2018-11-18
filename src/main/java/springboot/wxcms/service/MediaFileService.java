
package springboot.wxcms.service;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.mapper.MediaFilesMapper;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MediaFileService {

	@Resource
	private MediaFilesMapper mediaFilesMapper;
	@Resource
	private MsgBaseMapper baseDao;

	public void add(MediaFiles entity) {
		MsgBase base = new MsgBase();
		base.setCreateTime(LocalDateTime.now());
		base.setMsgtype(entity.getMediaType());
		baseDao.add(base);
		//关联回复表
		entity.setBaseId(base.getId());
		//需要对base表添加数据
		mediaFilesMapper.(entity);
	}

	public void deleteByMediaId(String mediaId) {
		MediaFiles media = mediaFilesMapper.selectByPrimaryKey(mediaId);
		MsgBase base = new MsgBase();
		base.setId(media.getBaseId());
		baseDao.delete(base);
		mediaFilesMapper.deleteByPrimaryKey(mediaId);
	}

	public MediaFiles getFileByMediaId(String mediaId) {
		return mediaFilesMapper.selectByPrimaryKey(mediaId);
	}

	public List<MediaFiles> getMediaListByPage(MediaFiles entity) {
		PageHelper.startPage(entity.getPageNum(),entity.getPageSize());
		return mediaFilesMapper.getMediaListByPage(entity);
	}

}
