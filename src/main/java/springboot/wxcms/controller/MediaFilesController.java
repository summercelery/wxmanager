
package springboot.wxcms.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.core.util.PropertiesUtil;
import springboot.wxapi.process.MpAccount;
import springboot.wxapi.process.WxApi;
import springboot.wxapi.process.WxApiClient;
import springboot.wxapi.process.WxMemoryCacheClient;
import springboot.wxcms.entity.ImgResource;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.ImgResourceService;
import springboot.wxcms.service.MediaFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 语音和视频控制器
 */
@RestController
@RequestMapping("mediaFile")
public class MediaFilesController  {

	@Autowired
	private MediaFileService mediaFileService;
	@Autowired
	private ImgResourceService imgResourceService;
	
	/**
	 * 分页展示
	 * @param searchEntity
	 * @return
	 */
    @RequestMapping(value = "/list")
    public Result list(MediaFiles searchEntity) {
        List<MediaFiles> pageList = mediaFileService.getMediaListByPage(searchEntity);
        return Result.ok(pageList);
    }
    /**
     * 添加视频素材
     * @param mediaFile
     * @return
     * @throws WxErrorException
     */
    @RequestMapping(value = "/addVideo")
    public Result addVideo(MediaFiles mediaFile) throws WxErrorException{
    	MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();
    	String accessToken = WxApiClient.getAccessToken(mpAccount);
    	Map<String,String> params=new HashMap<>();
    	JSONObject json=new JSONObject();
    	json.put("title", mediaFile.getTitle());
    	json.put("introduction", mediaFile.getIntroduction());
    	params.put("description", json.toJSONString());
    	JSONObject result = WxApi.addMateria(accessToken, "video", mediaFile.getUrl(), params);
    	mediaFile.setMediaId(result.getString("media_id"));
    	mediaFile.setMediaType("video");
    	mediaFile.setCreateTime(LocalDateTime.now());
    	mediaFile.setUpdateTime(LocalDateTime.now());
    	mediaFileService.add(mediaFile);
    	return Result.ok();
    }
    /**
     *  删除素材（微信端和本地数据库）
     * @param mediaId
     * @return
     * @throws WxErrorException
     */
	@RequestMapping("delMediaFile")
    public Result delMediaFile(String mediaId) throws WxErrorException{
		WxApiClient.deleteMaterial(mediaId,WxMemoryCacheClient.getMpAccount());
    	mediaFileService.deleteByMediaId(mediaId);
    	return Result.deleteSuccess();
    }
    
    /**
     *  上传素材文件到本地
     * @param file
     * @return
     * @throws Exception
     */
	@RequestMapping("uploadFile")
	public Result uploadFile(MultipartFile file,HttpServletRequest request) throws Exception {
		//原文件名称
		String trueName = file.getOriginalFilename();
		//文件后缀名
		String ext = FilenameUtils.getExtension(trueName);

		//系统生成的文件名
		String fileName = file.getOriginalFilename();
		fileName = System.currentTimeMillis() + new Random().nextInt(10000) + "." + ext;
		//文件上传路径
		String resURL = PropertiesUtil.getString("res.upload.url").toString();
		String filePath = request.getSession().getServletContext().getRealPath("/");

		//读取配置文上传件的路径
		if (PropertiesUtil.getString("res.upload.path") != null) {
			filePath = PropertiesUtil.getString("res.upload.path").toString() + fileName;
		} else {
			filePath = filePath + "/upload/" + fileName;
		}

		File saveFile = new File(filePath);

		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		file.transferTo(saveFile);
		//构造返回参数
		Map<String, Object> mapData = new HashMap();
		mapData.put("src", resURL + fileName);//文件url
		mapData.put("url", filePath);//文件绝对路径url
		mapData.put("title", fileName);//图片名称，这个会显示在输入框里
		
		return Result.ok(mapData);
    }
    /**
     *  添加语音\图片\缩略图素材
     * @param file
     * @return
     * @throws Exception
     */
	@RequestMapping("addMateria")
	public Result addMateria(MultipartFile file, String type, HttpServletRequest request) throws Exception {
    	JSONObject obj = new JSONObject();
    	if (null == file) {
			obj.put("message", "没有文件上传");
		}
    	//原文件名称
		String trueName = file.getOriginalFilename();
		//文件后缀名
		String ext = FilenameUtils.getExtension(trueName);

		//系统生成的文件名
		String fileName = file.getOriginalFilename();
		fileName = System.currentTimeMillis() + new Random().nextInt(10000) + "." + ext;
		//文件上传路径
		String resURL = PropertiesUtil.getString("res.upload.url").toString();
		String filePath = request.getSession().getServletContext().getRealPath("/");

		//读取配置文上传件的路径
		if (PropertiesUtil.getString("res.upload.path") != null) {
			filePath = PropertiesUtil.getString("res.upload.path").toString() + fileName;
		} else {
			filePath = filePath + "/upload/" + fileName;
		}

		File saveFile = new File(filePath);

		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		file.transferTo(saveFile);
		
		
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();
    	String accessToken = WxApiClient.getAccessToken(mpAccount);
    	JSONObject result = WxApi.addMateria(accessToken, type, filePath,null );
    	String mediaId = result.getString("media_id");
    	//图片或者图文的缩略图
    	if(type.equals("image")||type.equals("thumb")){
    		//图片url
    		String imgUrl = result.getString("url");
    		ImgResource img = new ImgResource();
    		img.setName(fileName);
    		img.setSize((int) file.getSize());
    		img.setTrueName(trueName);
    		img.setType(type);//这里用来区分image和thumb
    		img.setUrl(resURL + fileName);
    		img.setHttpUrl(imgUrl);
    		img.setMediaId(mediaId);
    		
    		String imgRes = this.imgResourceService.addImg(img);
    		obj.put("url", imgRes);
			obj.put("imgMediaId", mediaId);
			return Result.ok(obj);
    	}else {//音频 voice
	    	MediaFiles mediaFile =new MediaFiles();
	    	mediaFile.setUploadUrl(resURL + fileName);
	    	mediaFile.setUrl(filePath);
	    	mediaFile.setTitle(fileName);//用title保存文件名
	    	mediaFile.setMediaId(mediaId);
	    	mediaFile.setMediaType(type);
	    	mediaFile.setCreateTime(LocalDateTime.now());
	    	mediaFile.setUpdateTime(LocalDateTime.now());
	    	mediaFileService.add(mediaFile);
			return Result.saveSuccess();
    	}
    }
}
