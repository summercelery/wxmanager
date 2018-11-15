
package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.mapper.MediaFilesMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MediaFileService implements MediaFileService {

	@Resource
	private MediaFilesMapper mediaFilesMapper;
	@Resource
	private MsgBaseMapper baseDao;
	
	@Override
	public void add(MediaFiles entity) {
		MsgBase base = new MsgBase();
		base.setCreateTime(new Date());
		base.setMsgtype(entity.getMediaType());
		baseDao.add(base);
		//关联回复表
		entity.setBaseId(base.getId());
		//需要对base表添加数据
		mediaFilesDao.add(entity);
	}

	@Override
	public void deleteByMediaId(String mediaId) {
		MediaFiles media = mediaFilesMapper.getFileByMediaId(mediaId);
		MsgBase base = new MsgBase();
		base.setId(media.getBaseId());
		baseDao.delete(base);
		mediaFilesMapper.deleteByMediaId(mediaId);
	}

	@Override
	public MediaFiles getFileByMediaId(String mediaId) {
		return mediaFilesMapper.getFileByMediaId(mediaId);
	}

	@Override
	public List<MediaFiles> getMediaListByPage(MediaFiles entity) {
		return mediaFilesMapper.getMediaListByPage(entity);
	}

}
