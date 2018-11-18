
package springboot.wxcms.service;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.core.constant.Constants;
import springboot.core.util.CommonUtil;
import springboot.wxapi.process.MediaType;
import springboot.wxcms.entity.ImgResource;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.entity.Page;
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
	private MediaFilesMapper mediaFilesMapper;
	@Resource
	private MsgBaseMapper msgBaseMapper;
	
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
		imgResourceMapper.insert(img);

		//添加base表
		MsgBase base = new MsgBase();
		base.setCreateTime(LocalDateTime.now());
		base.setMsgtype(MediaType.Image.name());
		msgBaseMapper.add(base);
		//添加到素材表中
		MediaFiles entity = new MediaFiles();
		entity.setMediaId(img.getMediaId());
		entity.setMediaType(MediaType.Image.name());
		entity.setBaseId(base.getId());
		entity.setCreateTime(LocalDateTime.now());
		entity.setUpdateTime(LocalDateTime.now());
		mediaFilesMapper.insert(entity);
		return img.getUrl();
	}

	public List<ImgResource> getImgListByPage(ImgResource entity) {
		PageHelper.startPage(entity.getPageNum(),entity.getPageSize());
		return imgResourceMapper.getImgListByPage(entity);
	}
	
	public boolean removeOtherToImg(String otherId) {
		return false;
	}


	public boolean updateImgFlag(String id, Integer flag) {
		return false;
	}

	public boolean delImg(String id) {
		ImgResource img = imgResourceMapper.selectByPrimaryKey(id);
//		MsgBase base = new MsgBase();
//		base.setId(img.);
//		baseDao.delete(base);
		mediaFilesMapper.deleteByPrimaryKey(img.getMediaId());
		imgResourceMapper.deleteByPrimaryKey(img.getMediaId());
		
		return true;
	}



}
