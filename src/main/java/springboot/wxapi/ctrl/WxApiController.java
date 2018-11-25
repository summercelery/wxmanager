
package springboot.wxapi.ctrl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springboot.core.exception.WxErrorException;
import springboot.core.util.DateUtil;
import springboot.core.util.UploadUtil;
import springboot.core.util.wx.HTTPResultXml;
import springboot.core.util.wx.SignUtil;
import springboot.core.util.wx.TenpayUtil2;
import springboot.wxapi.process.*;
import springboot.wxapi.service.MyService;
import springboot.wxapi.vo.*;
import springboot.wxcms.entity.*;
import springboot.wxcms.service.MsgNewsService;
import springboot.wxcms.service.MsgTextService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * 微信与开发者服务器交互接口
 */
@Controller
@RequestMapping("/wxapi")
@Slf4j
public class WxApiController {
	

	@Resource
	private MyService myService;
	@Resource
	private MsgTextService msgTextService;
	@Resource
	private MsgNewsService msgNewsService;
	
	/**
	 * GET请求：进行URL、Tocken 认证；
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序
	 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
	@RequestMapping(value = "/{account}/message",  method = RequestMethod.GET)
	public @ResponseBody String doGet(HttpServletRequest request,@PathVariable String account) {
		//如果是多账号，根据url中的account参数获取对应的MpAccount处理即可
		
		Set<String> keySet = request.getParameterMap().keySet();
		Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()){  
            //如果存在，则调用next实现迭代  
            String key=iterator.next();    
            log.info("key: " + key + " value: " + request.getParameterMap().get(key));
        }
		
		
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			String token = mpAccount.getToken();//获取token，进行验证；
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			String echostr = request.getParameter("echostr");// 随机字符串
			
			// 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
			if (SignUtil.validSign(signature, token, timestamp, nonce)) {
				return echostr;
			}
		}
		return "error";
	}
	
	/**
	 * POST 请求：进行消息处理；
	 * */
	@RequestMapping(value = "/{account}/message", method = RequestMethod.POST)
	public @ResponseBody String doPost(HttpServletRequest request,@PathVariable String account,HttpServletResponse response) {
		//处理用户和微信公众账号交互消息
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount(account);
		try {
				MsgRequest msgRequest = MsgXmlUtil.parseXml(request);//获取发送的消息
			return myService.processMsg(msgRequest,mpAccount);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return "error";
		}
	}
	
	//创建微信公众账号菜单
	@RequestMapping(value = "/publishMenu")
	public ModelAndView publishMenu() throws WxErrorException {
		JSONObject rstObj = null;
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();
		if(mpAccount != null){
			rstObj = myService.publishMenu(mpAccount);
			if(rstObj != null){//成功，更新菜单组
				if(rstObj.containsKey("menu_id")){
					ModelAndView mv = new ModelAndView("common/ok");
					mv.addObject("okMsg", "创建菜单成功");
					return mv;
				}else if(rstObj.containsKey("errcode") && rstObj.getIntValue("errcode") == 0){
					ModelAndView mv = new ModelAndView("common/ok");
					mv.addObject("okMsg", "创建菜单成功");
					return mv;
				}
			}
		}
		
		ModelAndView mv = new ModelAndView("common/failure");
		String failureMsg = "创建菜单失败，请检查菜单：可创建最多3个一级菜单，每个一级菜单下可创建最多5个二级菜单。";
		if(rstObj != null){
			failureMsg += ErrCode.errMsg(rstObj.getIntValue("errcode"));
		}
		mv.addObject("failureMsg", failureMsg);
		return mv;
	}
	
	//删除微信公众账号菜单
	@RequestMapping(value = "/deleteMenu")
	public ModelAndView deleteMenu(HttpServletRequest request) throws WxErrorException {
		JSONObject rstObj = null;
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			rstObj = myService.deleteMenu(mpAccount);
			if(rstObj != null && rstObj.getIntValue("errcode") == 0){
				ModelAndView mv = new ModelAndView("common/ok");
				mv.addObject("okMsg", "删除菜单成功");
				return mv;
			}
		}
		ModelAndView mv = new ModelAndView("common/failure");
		String failureMsg = "删除菜单失败";
		if(rstObj != null){
			failureMsg += ErrCode.errMsg(rstObj.getIntValue("errcode"));
		}
		mv.addObject("failureMsg", failureMsg);
		return mv;
	}
	
	//获取用户列表
	@RequestMapping(value = "/syncAccountFansList")
	@ResponseBody
	public Result syncAccountFansList() throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			boolean flag = myService.syncAccountFansList(mpAccount);
			if(flag){
				return Result.ok();
			}
		}
		return Result.fail("操作失败");
	}

	/**
	 * 同步用户标签列表
	 * @return
	 */
	@RequestMapping(value = "/syncUserTagList")
	@ResponseBody
	public Result syncUserTagList() throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			boolean flag = myService.syncUserTagList(mpAccount);
			if(flag){
				return Result.ok();
			}
		}
		return Result.fail("操作失败");
	}

	//根据用户的ID更新用户信息
	@RequestMapping(value = "/syncAccountFans")
	@ResponseBody
	public Result syncAccountFans(String openId) throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if (mpAccount != null) {
			AccountFans fans = myService.syncAccountFans(openId, mpAccount, true);//同时更新数据库
			if (fans != null) {
				return Result.ok(fans);
			}
		}
		return Result.fail("操作失败");
	}
	
	//获取永久素材
	@RequestMapping(value = "/syncMaterials")
	public Result syncMaterials(MaterialArticle materialArticle) throws WxErrorException {
		List<MaterialArticle> materialList = new ArrayList<MaterialArticle>();
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		Material material = WxApiClient.syncBatchMaterial(MediaType.News, materialArticle.getPageNum(), materialArticle.getPageSize(),mpAccount);
		if(material != null){

			List<MaterialItem> itemList = material.getItems();
			if(itemList != null){
				for(MaterialItem item : itemList){
					MaterialArticle m = new MaterialArticle();
					if(item.getNewsItems() != null && item.getNewsItems().size() > 0){
						MaterialArticle ma = item.getNewsItems().get(0);//用第一个图文的简介、标题、作者、url
						m.setAuthor(ma.getAuthor());
						m.setTitle(ma.getTitle());
						m.setUrl(ma.getUrl());
					}
					materialList.add(m);
				}
			}
		}

		Result result = Result.ok(materialList);
		Page newPage = new Page();
		newPage.setPageNum(materialArticle.getPageNum());
		newPage.setPageSize(materialArticle.getPageSize());
		newPage.setTotal(materialArticle.getTotal());
		newPage.setPages(materialArticle.getPages());
		result.setPage(newPage);
		return result;
	}
	
	
	//上传图文素材
	@RequestMapping(value = "/doUploadMaterial")
	public  ModelAndView doUploadMaterial(MsgNews msgNews) throws Exception {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		String rstMsg = "上传图文消息素材";
		List<MsgNews> msgNewsList = new ArrayList<MsgNews>();
		msgNewsList.add(msgNews);
		JSONObject rstObj = WxApiClient.uploadNews(msgNewsList, mpAccount);
		if(rstObj.containsKey("media_id")){
			ModelAndView mv = new ModelAndView("common/ok");
			mv.addObject("okMsg", "上传图文素材成功,素材 media_id : " + rstObj.getString("media_id"));
			return mv;
		}else{
			rstMsg = ErrCode.errMsg(rstObj.getIntValue("errcode"));
		}
		ModelAndView mv = new ModelAndView("common/failure");
		mv.addObject("failureMsg", rstMsg);
		return mv;
	}
	
	//获取openid
	@RequestMapping(value = "/oauthOpenid.html")
	public ModelAndView oauthOpenid(HttpServletRequest request) throws WxErrorException {
		log.info("-------------------------------------oauthOpenid-----<0>-------------------");
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		log.info("-------------------------------------oauthOpenid-----<1>-------------------mpAccount："+mpAccount.getAccount());
		if(mpAccount != null){
			ModelAndView mv = new ModelAndView("wxweb/oauthopenid");
			//拦截器已经处理了缓存,这里直接取
			String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());
			log.info("-------------------------------------oauthOpenid-----<2>-------------------openid:"+openid);
			AccountFans fans = myService.syncAccountFans(openid,mpAccount,false);//同时更新数据库
			mv.addObject("openid", openid);
			mv.addObject("fans", fans);
			log.info("-------------------------------------oauthOpenid-----<3>-------------------fans:"+fans.getNicknameStr());
			return mv;
		}else{
			ModelAndView mv = new ModelAndView("common/failuremobile");
			mv.addObject("message", "OAuth获取openid失败");
			log.info("-------------------------------------oauthOpenid-----<4>-------------------");
			return mv;
		}
	}
	
	/**
	 * 生成二维码
	 * @param request
	 * @param num 二维码参数
	 * @return
	 */
	@RequestMapping(value = "/createQrcode", method = RequestMethod.POST)
	public ModelAndView createQrcode(HttpServletRequest request,Integer num) throws WxErrorException {
		ModelAndView mv = new ModelAndView("wxcms/qrcode");
		mv.addObject("cur_nav", "qrcode");
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(num != null){
			byte[] qrcode = WxApiClient.createQRCode(60,num,mpAccount);//有效期60s
			String url = UploadUtil.byteToImg(request.getServletContext().getRealPath("/"), qrcode);
			mv.addObject("qrcode", url);
		}
		mv.addObject("num", num);
		return mv;
	}
	
	//以根据openid群发文本消息为例
	@RequestMapping(value = "/massSendTextMsg", method = RequestMethod.POST)
	public void massSendTextMsg(HttpServletResponse response,String openid,String content) throws WxErrorException {
		content = "群发文本消息";
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		String rstMsg = "根据openid群发文本消息失败";
		if(mpAccount != null && !StringUtils.isBlank(openid)){
			List<String> openidList = new ArrayList<String>();
			openidList.add(openid);
			//根据openid群发文本消息
			JSONObject result = WxApiClient.massSendTextByOpenIds(openidList, content, mpAccount);
			try {
				if(result.getIntValue("errcode") != 0){
					response.getWriter().write("send failure");
				}else{
					response.getWriter().write("send ok");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ModelAndView mv = new ModelAndView("common/failure");
		mv.addObject("failureMsg", rstMsg);
	}
	
	/**
	 * 发送客服消息
	 * @param openid ： 粉丝的openid
	 * @return
	 */
	@RequestMapping(value = "/sendCustomTextMsg", method = RequestMethod.POST)
	public void sendCustomTextMsg(HttpServletRequest request,HttpServletResponse response,String openid) throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		String content = "微信派官方测试客服消息";
		JSONObject result = WxApiClient.sendCustomTextMessage(openid, content, mpAccount);
		try {
			if(result.getIntValue("errcode") != 0){
				response.getWriter().write("send failure");
			}else{
				response.getWriter().write("send ok");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送模板消息
	 * @return
	 */
	@RequestMapping(value = "/sendTemplateMessage", method = RequestMethod.POST)
	@ResponseBody
	public Result sendTemplateMessage(String openIds) throws WxErrorException  {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		TemplateMessage tplMsg = new TemplateMessage();

		String[] openIdArray = StringUtils.split(openIds, ",");
		for (String openId : openIdArray) {
			tplMsg.setOpenid(openId);
			//微信公众号号的template id，开发者自行处理参数
			tplMsg.setTemplateId("azx4q5sQjWUk1O3QY0MJSJkwePQmjR-T5rCyjyMUw8U");

			tplMsg.setUrl("https://www.smartwx.info");
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("first", "smartadmin管理后台已经上线，欢迎吐槽");
			dataMap.put("keyword1", "时间：" + DateUtil.changeDateTOStr(new Date()));
			dataMap.put("keyword2", "码云平台地址：https://gitee.com/qingfengtaizi/wxmp");
			dataMap.put("keyword3", "github平台地址：https://github.com/qingfengtaizi/wxmp-web");
			dataMap.put("remark", "我们期待您的加入");
			tplMsg.setDataMap(dataMap);

			JSONObject result = WxApiClient.sendTemplateMessage(tplMsg, mpAccount);
		}

		return Result.ok();
	}
	
	/**
	 * 微信异步返回
	 * @param requestBodyXml
	 * @return
	 */
	@RequestMapping(value = "/wxipay_noity")
	public @ResponseBody HTTPResultXml wxipay_noity(@RequestBody String requestBodyXml) {
		log.info("-------------------------------------wxipay_noity-----<0>-------------------requestBodyXml:"+requestBodyXml);		
		
		String jsonStr = requestBodyXml;
		Map<String, Object> dataMap = new HashMap<String, Object>();
	
		Map map = new HashMap();
		try {
			map = TenpayUtil2.doXMLParseByDom4j(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String return_code = (String) map.get("return_code");
		String return_msg = (String) map.get("return_msg");
		if("ok".equals(return_code)){
			String transaction_id = (String) map.get("transaction_id");
			String out_trade_no = (String) map.get("out_trade_no");
			
		    return_code="<![CDATA[ok]]>";
		    return_msg="<![CDATA[OK]]>";		
		}else{
			 return_msg= (String) map.get("err_code_des");
			 return_code="<![CDATA[FAIL]]>";
			 return_msg="<![CDATA["+return_msg+"]]>";	
		}		  
	
		
		HTTPResultXml httpResultXml=new HTTPResultXml();
		httpResultXml.setReturn_code(return_code);
		httpResultXml.setReturn_msg(return_msg);
		
		//String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());//先从缓存中获取openid
	
	/*	if(!array_key_exists("transaction_id", $data)){
			$msg = "输入参数不正确";
			return false;
		}
		//查询订单，判断订单真实性
		if(!$this->Queryorder($data["transaction_id"])){
			$msg = "订单查询失败";
			return false;
		}
		*/
		return httpResultXml;
	}

	
    /**
     * 给粉丝发送文本消息
     * @param msgId
     * @param openid
     * @return
     */
	@RequestMapping(value = "/sendTextMsgByOpenId", method = RequestMethod.POST)
	@ResponseBody
	public Result sendTextMsgByOpenId(String msgId, String openid) throws WxErrorException  {
		MsgText msgText = msgTextService.getById(msgId);
		String content = msgText.getContent();
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject result = WxApiClient.sendCustomTextMessage(openid, content, mpAccount);

		if (result.getIntValue("errcode") != 0) {
			return Result.fail(result.toString());
		} else {
			return Result.ok();
		}
	}

	/**
	 * 客服接口-发送图文消息
	 *
	 * @param id
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/sendNewsByOpenId", method = RequestMethod.POST)
	@ResponseBody
	public Result sendNewsByOpenId(String id, String openid) throws WxErrorException {
		
		MsgNews msgNews  = this.msgNewsService.getById(id);

		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject result = WxApiClient.sendCustomNews(openid, msgNews, mpAccount);
		log.info(" 客服接口-发送图文消息：" + result.toString());
		if (result.getIntValue("errcode") != 0) {
			return Result.fail(result.toString());
		} else {
			return Result.ok();
		}
	}
	
    /**
     * 客服接口 -批量发送文本消息
     * @param textId
     * @param openIds
     * @return
     */
	@RequestMapping(value = "/batchSendText", method = RequestMethod.POST)
	@ResponseBody
	public String batchSendText(String textId,String openIds) throws WxErrorException {
		String code = "1";
		MsgText msgText = msgTextService.getById(textId);
		String[] openIdAarry = {"0"};
		if(openIds.contains(",")){
			openIdAarry = openIds.split(",");
		}else{
			openIdAarry[0] = openIds;
		}
		String content = msgText.getContent();
		
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		
		for(int i = 0; i<openIdAarry.length; i++){
			String openid = openIdAarry[i];
			JSONObject result = WxApiClient.sendCustomTextMessage(openid, content, mpAccount);
		}

		return code;
	}
	
	/**
     * 群发-文本消息
     * @param textId
     * @param openIds
     * @return
     */
	@RequestMapping(value = "/massSendTextByOpenIds", method = RequestMethod.POST)
	@ResponseBody
	public String massSendTextByOpenIds(String textId,String openIds) throws WxErrorException {
		String code = "0";
		MsgText msgText = msgTextService.getById(textId);
		//分隔字符串
		String[] openIdAarry = openIds.split(",");

		String content = msgText.getContent();
		
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		//openids
		List<String> openidList = new ArrayList<String>();
		for(int i = 0; i<openIdAarry.length; i++){
			String openid = openIdAarry[i];
			openidList.add(openid);
		}
		JSONObject result = WxApiClient.massSendTextByOpenIds(openidList,content,mpAccount);
		log.info(" 群发-文本消息："+result.toString());
		if(result.getIntValue("errcode") != 0){
			code = result.toString();//发送失败
		}else{
			code = "1";//发送成功
		}
		return code;
	}
	
	/**
	 * 高级群发-图文消息|
	 * @param newsId
	 * @param openIds
	 * @return
	 */
	@RequestMapping(value = "/massSendNewsByOpenIds", method = RequestMethod.POST)
	@ResponseBody
	public String massSendNewsByOpenIds(String newsId,String openIds) throws WxErrorException {
		String code = "";
        MsgNews msgNews = this.msgNewsService.getById(newsId);
		
		List<MsgNews> msgNewsList = new ArrayList<MsgNews>();
		msgNewsList.add(msgNews);
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		//先上传图文素材
		//JSONObject newsObject = WxApiClient.uploadNews(msgNewsList, mpAccount);
		//媒体id
        String media_id = msgNews.getMediaId();

		//分隔字符串
		String[] openIdAarry = openIds.split(",");
		//openids
		List<String> openidList = new ArrayList<String>();
		for(int i = 0; i<openIdAarry.length; i++){
			String openid = openIdAarry[i];
			openidList.add(openid);
		}
		JSONObject massQesultObj = WxApiClient.massSendByOpenIds(openidList,media_id,MsgType.MPNEWS,mpAccount);
		
		if(massQesultObj.getIntValue("errcode") != 0){
			code = massQesultObj.toString();
		}else{
			code = "1";//发送成功
		}
		return code;
	}
	

	/**
	 * 高级群发-图文消息|
	 * @param mediaId
	 * @param openIds
	 * @return
	 */
	@RequestMapping(value = "/sendMaterialByOpenIds", method = RequestMethod.POST)
	@ResponseBody
	public String sendMaterialByOpenIds(String mediaId,String openIds) throws WxErrorException {
		String code = "";
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		//分隔字符串
		String[] openIdAarry = openIds.split(",");
		//openids
		List<String> openidList = new ArrayList<String>();
		for(int i = 0; i<openIdAarry.length; i++){
			String openid = openIdAarry[i];
			openidList.add(openid);
		}
		JSONObject massQesultObj = WxApiClient.massSendByOpenIds(openidList,mediaId,MsgType.MPNEWS,mpAccount);
		
		if(massQesultObj.getIntValue("errcode") != 0){
			code = massQesultObj.toString();
		}else{
			code = "1";//发送成功
		}
		return code;
	}
	
	//创建微信公众账号菜单
	@RequestMapping(value = "/doPublishMenu")
	@ResponseBody
	public Result doPublishMenu() throws WxErrorException  {
		JSONObject rstObj = null;
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();
		if(mpAccount != null){
			rstObj = myService.publishMenu(mpAccount);
			if(rstObj != null){//成功，更新菜单组
				return Result.ok();
//				if(rstObj.containsKey("menu_id")){
//					ModelAndView mv = new ModelAndView("common/ok");
//					mv.addObject("okMsg", "创建菜单成功");
//					code = "1";
//					return code;
//				}else if(rstObj.containsKey("errcode") && rstObj.getIntValue("errcode") == 0){
//					ModelAndView mv = new ModelAndView("common/ok");
//					mv.addObject("okMsg", "创建菜单成功");
//					code = "1";
//					return code;
//				}
			}
		}
		return Result.fail("操作失败");
	}
	
	
	//删除微信公众账号菜单
	@RequestMapping(value = "/deletePublicMenu")
	@ResponseBody
	public String deletePublicMenu(HttpServletRequest request) throws WxErrorException  {
		String code = "";
		JSONObject rstObj = null;
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			rstObj = myService.deleteMenu(mpAccount);
			if(rstObj != null && rstObj.getIntValue("errcode") == 0){
				code = "1";
				return code;
			}
		}
		
		String failureMsg = "删除菜单失败";
		if(rstObj != null){
			failureMsg += ErrCode.errMsg(rstObj.getIntValue("errcode"));
		}
		code = failureMsg;
		return code;
	}
	/**
	 * 统计分析
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return
	 * @throws WxErrorException
	 */
	@RequestMapping(value = "/dataCube")
	@ResponseBody
	public Result dataCube(String type,String start,String end) throws WxErrorException  {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		String accessToken = WxApiClient.getAccessToken(mpAccount);
		JSONObject result = WxApi.forDataCube(accessToken, type, start, end);
		return Result.ok(result);
	}
}