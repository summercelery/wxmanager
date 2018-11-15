
package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.core.constant.Constants;
import springboot.core.util.CommonUtil;
import springboot.wxapi.process.MediaType;
import springboot.wxcms.entity.ImgResource;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.mapper.ImgResourceMapper;
import springboot.wxcms.mapper.MediaFilesMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ImgResourceService  {

	@Resource
    private ImgResourceMapper imgResourceMapper;
	@Resource
	private MediaFilesMapper mediaFilesDao;
	@Resource
	private MsgBase baseDao;
	
	public ImgResource getImg(String id) {
		return imgResourceMapper.selectByPrimaryKey(id);
	}

	public String addImg(ImgResource img) {
		img.setFlag(Constants.IMG_FLAG0);
		img.setCreateTime(LocalDateTime.now());
		img.setUpdateTime(LocalDateTime.now());
		//主键id
		String id = CommonUtil.getUID();
		img.setId(id);
		imgResourceDao.add(img);

		//添加base表
		MsgBase base = new MsgBase();
		base.setCreateTime(new Date());
		base.setMsgtype(MediaType.Image.name());
		baseDao.add(base);
		//添加到素材表中
		MediaFiles entity = new MediaFiles();
		entity.setMediaId(img.getMediaId());
		entity.setMediaType(MediaType.Image.name());
		entity.setBaseId(base.getId());
		entity.setCreateTime(new Date(System.currentTimeMillis()));
		entity.setUpdateTime(new Date(System.currentTimeMillis()));
		mediaFilesDao.add(entity);
		return img.getUrl();
	}

	@Override
	public List<ImgResource> getImgListByPage(ImgResource entity) {
		return imgResourceDao.getImgListByPage(entity);
	}
	
	@Override
	public boolean removeOtherToImg(String otherId) {
		return false;
	}


	@Override
	public boolean updateImgFlag(String id, Integer flag) {
		return false;
	}

	@Override
	public boolean delImg(String id) {
		ImgResource img = imgResourceDao.getImgById(id);
//		MsgBase base = new MsgBase();
//		base.setId(img.);
//		baseDao.delete(base);
		mediaFilesDao.deleteByMediaId(img.getMediaId());
		imgResourceDao.deleteByMediaId(img.getMediaId());
		
		return true;
	}



}
