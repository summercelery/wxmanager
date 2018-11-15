
package springboot.wxapi.process;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import springboot.core.util.CacheUtils;
import springboot.wxcms.entity.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存工具类；
 * 目前使用 服务器内存的方式；
 *
 * 1、开发者可以根据自己的需求使用不同的缓存方式,比如memcached
 *
 */
public class WxMemoryCacheClient {


	public static final String SESSION_ACCOUNT= "session_account";

	public static void addMpAccount(List<Account> accounts){
		Map<String,MpAccount> mpAccountMap =(Map<String,MpAccount>)CacheUtils.get("mpAccountMap");
		if(mpAccountMap==null){
			mpAccountMap= new HashMap<>();
		}
		if (CollectionUtils.isNotEmpty(accounts)) {
			for (MpAccount account:accounts) {
				mpAccountMap.put(account.getAccount(), account);
			}
		}
		CacheUtils.put("mpAccountMap", mpAccountMap);
	}
//
	public static MpAccount getMpAccount(){
		Map<String,MpAccount> mpAccountMap = (Map<String, MpAccount>) CacheUtils.get("mpAccountMap");
		return mpAccountMap.get(getAccount());
	}

	public static MpAccount getMpAccount(String accout){
		Map<String,MpAccount> mpAccountMap = (Map<String, MpAccount>) CacheUtils.get("mpAccountMap");
		return mpAccountMap.get(accout);
	}

	public static AccessToken addAccessToken(String account ,AccessToken token){
		Map<String,AccessToken> accountAccessTokenMap = (Map<String, AccessToken>) CacheUtils.get("accountAccessTokenMap");
		if(accountAccessTokenMap==null){
			accountAccessTokenMap=new HashMap<>();
		}
		if (null == accountAccessTokenMap) {
			accountAccessTokenMap = new HashMap<>();
		}
		if(token != null){
			accountAccessTokenMap.put(account, token);
		}
		CacheUtils.put("accountAccessTokenMap", accountAccessTokenMap);
		return token;
	}

	/**
	 * accessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * @return
	 */
	public static AccessToken getAccessToken(){
		Map<String,AccessToken> accountAccessTokenMap = (Map<String, AccessToken>) CacheUtils.get("accountAccessTokenMap");
		if(accountAccessTokenMap==null){
			accountAccessTokenMap=new HashMap<>();
		}
		return accountAccessTokenMap.get(getAccount());
	}

	/**
	 * 添加JSTicket到缓存
	 * @param account
	 * @param jsTicket
	 * @return
	 */
	public static JSTicket addJSTicket(String account ,JSTicket jsTicket){
		Map<String,JSTicket> accountJSTicketMap = (Map<String, JSTicket>) CacheUtils.get("accountJSTicketMap");
		if(null==accountJSTicketMap){
			accountJSTicketMap=new HashMap<>();
		}
		if(jsTicket != null){
			accountJSTicketMap.put(account, jsTicket);
		}
		CacheUtils.put("accountJSTicketMap", accountJSTicketMap);
		return jsTicket;
	}

	/**
	 * JSTicket的获取，绝对不要从缓存中直接获取，请从JSTicket中获取；
	 * @return
	 */
	public static JSTicket getJSTicket(){
		Map<String,JSTicket> accountJSTicketMap = (Map<String, JSTicket>) CacheUtils.get("accountJSTicketMap");
		if(null==accountJSTicketMap){
			accountJSTicketMap=new HashMap<>();
		}
		return accountJSTicketMap.get(getAccount());
	}

	//处理openid缓存
	public static String getOpenid(String sessionid){
		Map<String,String> sessionOpenIdMap = (Map<String,String>) CacheUtils.get("sessionOpenIdMap");
		if(null==sessionOpenIdMap){
			sessionOpenIdMap=new HashMap<>();
		}
		if(!StringUtils.isBlank(sessionid)){
			return sessionOpenIdMap.get(sessionid);
		}
		return null;
	}

	public static String setOpenid(String sessionid, String openid){
		Map<String,String> sessionOpenIdMap = (Map<String,String>) CacheUtils.get("sessionOpenIdMap");
		if(null==sessionOpenIdMap){
			sessionOpenIdMap=new HashMap<>();
		}
		if(!StringUtils.isBlank(sessionid) && !StringUtils.isBlank(openid)){
			sessionOpenIdMap.put(sessionid, openid);
		}
		CacheUtils.put("sessionOpenIdMap", sessionOpenIdMap);
		return openid;
	}

	//处理OAuth的Token
	public static AccessToken addOAuthAccessToken(String account ,OAuthAccessToken token){
		Map<String,OAuthAccessToken> accountOAuthTokenMap = (Map<String, OAuthAccessToken>) CacheUtils.get("accountOAuthTokenMap");
		if(null==accountOAuthTokenMap){
			accountOAuthTokenMap=new HashMap<>();
		}
		if(token != null){
			accountOAuthTokenMap.put(account, token);
		}
		CacheUtils.put("accountOAuthTokenMap", accountOAuthTokenMap);
		return token;
	}

	/**
	 * OAuthAccessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * @return
	 */
	public static OAuthAccessToken getOAuthAccessToken(){
		Map<String,OAuthAccessToken> accountOAuthTokenMap = (Map<String, OAuthAccessToken>) CacheUtils.get("accountOAuthTokenMap");
		if(null==accountOAuthTokenMap){
			accountOAuthTokenMap=new HashMap<>();
		}
		return accountOAuthTokenMap.get(getAccount());
	}

	/**
	 * 设置公众号信息 缓存中
	 * @param account
	 */
	public static void setAccount(String account) {
		CacheUtils.put(SESSION_ACCOUNT, account);
	}
	/**
	 * 从缓存中获取公众号
	 * @return String
	 */
	public static String getAccount() {
		return (String) CacheUtils.get(SESSION_ACCOUNT);
	}
}
