
package springboot.wxcms.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.constant.Constants;
import springboot.core.exception.WxErrorException;
import springboot.wxapi.process.MpAccount;
import springboot.wxapi.process.WxApiClient;
import springboot.wxapi.process.WxMemoryCacheClient;
import springboot.wxcms.entity.AccountFans;
import springboot.wxcms.entity.Result;
import springboot.wxcms.entity.UserTag;
import springboot.wxcms.service.AccountFansService;
import springboot.wxcms.service.UserTagService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/userTag")
public class UserTagController {

	@Autowired
	private UserTagService userTagService;
	@Autowired
	private AccountFansService accountFansService;
	
	/**
	 * 根据用户标签获取粉丝列表
	 * @return
	 * @throws WxErrorException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUserListByTag")
	public Result getUserListByTag(Integer id) throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject  tagId = new JSONObject();
		tagId.put("tagid", id);
		tagId.put("next_openid", "");
		JSONObject jsonObjUserList = WxApiClient.getUserListByTag(tagId.toString(), mpAccount);
		if(jsonObjUserList !=null && jsonObjUserList.containsKey("data") ) {
			JSONObject  data =  (JSONObject) jsonObjUserList.get("data");
			JSONArray openidArray = data.getJSONArray("openid");
			String js=JSONObject.toJSONString(openidArray);
			List<String> list = JSONObject.parseArray(js, String.class);
			List<AccountFans> fansList = new ArrayList<AccountFans>();
			for (String  openId : list) {
				AccountFans fans = new AccountFans();
				fans.setOpenId(openId);
				fansList.add(fans);
			}
			 fansList = accountFansService.getFansByOpenIdListByPage(fansList);
			return Result.ok(fansList);
		}

		return Result.fail("没有数据");
	}

	/**
	 * 根据Id查询用户标签
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getById")
	public Result getById(String id){
		UserTag userTag = userTagService.getById(id);
		return Result.ok(userTag);
	}

	/**
	 * 分页查询
	 * @param searchEntity
	 * @return
	 */
	@RequestMapping(value = "/listForPage")
	public Result listForPage(UserTag searchEntity) {
		List<UserTag> list = userTagService.listForPage(searchEntity);
		if (CollectionUtils.isEmpty(list)) {
			return Result.ok();
		}
		return Result.ok(list);
	}

	/**
	 * 修改/添加
	 * @param entity
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/update")
	public Result update(UserTag entity) throws WxErrorException{
		if (entity.getId() != null) {
			userTagService.update(entity);
			//更新成功
			return Result.ok();
		} else {
			//添加分两步
			//1. 调用微信API添加 
			MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
			JSONObject  tagName = new JSONObject();
			tagName.put("name", entity.getName());
			JSONObject  tagjson = new JSONObject();
			tagjson.put("tag", tagName);
			String userTags = tagjson.toString();
			JSONObject userTag = WxApiClient.createUserTag(userTags, mpAccount);
			//2.添加到本地库
			if(userTag != null) {
				JSONObject returnUserTag = (JSONObject) userTag.get("tag");
				entity = JSONObject.parseObject(returnUserTag.toJSONString(), UserTag.class);
				userTagService.add(entity);
				return Result.ok();
			}
			return Result.fail(Constants.MSG_ERROR);
		}
	}

	/**
	 * 删除
	 * @param entity
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/deleteById")
	public Result deleteById(UserTag entity) {
		//1.删除微信服务器的用户标签
		if(deleteUserTag(entity.getId())){
			//2.删除本地数据库的用户标签
			userTagService.delete(entity);
			return Result.ok();
		}
		return Result.fail("用户标签删除失败！");
	}
	
	/**
	 * 批量删除
	 * @param String[] ids
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/deleteBatchIds")
	public Result deleteBatchIds(String [] ids)  {
		if(null != ids && ids.length>0) {
			int nums = 0;
			for (String id : ids) {
				if(deleteUserTag(id)) {
					nums++;
				}
			}
			if(nums == ids.length) {
				userTagService.deleteBatchIds(ids);
			}
			return Result.ok();
		}else {
			return Result.fail("用户标签批量删除失败");
		}
	}
	/**
	 * 删除微信服务器的用户标签
	 * @param id
	 * @return boolean
	 */
	private boolean deleteUserTag(String id) {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject  tag = new JSONObject();
		JSONObject  tagId = new JSONObject();
		tagId.put("id", id);
		tag.put("tag", tagId);
		JSONObject result=null ;
		try {
			result = WxApiClient.deleteUserTag(tag.toJSONString(), mpAccount);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		if(result!=null && result.containsKey("errmsg")) {
			if("ok".equals(result.get("errmsg").toString())){
				return true;
			}
		}
		return false;
	}
}
