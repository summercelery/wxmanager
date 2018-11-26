
package springboot.core.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import springboot.core.util.HttpUtil;
import springboot.wxapi.process.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信客户端用户请求验证拦截器
 */
@Slf4j
public class WxOAuth2Interceptor extends HandlerInterceptorAdapter {

	/**
	 * 开发者自行处理拦截逻辑，
	 * 方便起见，此处只处理includes
	 */
	private String[] excludes;//不需要拦截的
	private String[] includes;//需要拦截的
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		
		log.info("-------------------------------------preHandle-----<0>-------------------uri:"+uri);			
		
		boolean oauthFlag = false;//为方便展示的参数，开发者自行处理
		for(String s : includes){
			if(uri.contains(s)){//如果包含，就拦截
				oauthFlag = true;
				break;
			}
		}
		if(!oauthFlag){//如果不需要oauth认证
			return true;
		}
		
		String sessionid = request.getSession().getId();
		String openid = WxMemoryCacheClient.getOpenid(sessionid);//先从缓存中获取openid
		log.info("-------------------------------------preHandle-----<1>-------------------openid:"+openid);		
		
		if(StringUtils.isBlank(openid)){//没有，通过微信页面授权获取
			String code = request.getParameter("code");
			log.info("-------------------------------------preHandle-----<2-1>-------------------code:"+code);		

			if(!StringUtils.isBlank(code)){//如果request中包括code，则是微信回调
				log.info("-------------------------------------preHandle-----<2-2>-------------------code:"+code);		
				try {
					openid = WxApiClient.getOAuthOpenId(WxMemoryCacheClient.getMpAccount(), code);
					log.info("-------------------------------------preHandle-----<2-3>-------------------openid:"+openid);	
					if(!StringUtils.isBlank(openid)){
						WxMemoryCacheClient.setOpenid(sessionid, openid);//缓存openid
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{//oauth获取code

				MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
				log.info("-------------------------------------preHandle-----<3-1>-------------------mpAccount:"+mpAccount.getAccount());	
				String redirectUrl = HttpUtil.getRequestFullUriNoContextPath(request);//请求code的回调url
				if(!HttpUtil.existHttpPath(redirectUrl)){
					//以上不存在就拼接全部url（包括context）
					redirectUrl=HttpUtil.getRequestFullUri(request);
				}
				log.info("-------------------------------------preHandle-----<3-2>-------------------redirectUrl:"+redirectUrl);
				String state = OAuth2RequestParamHelper.prepareState(request);
				log.info("-------------------------------------preHandle-----<3-3>-------------------state:"+state);
				String url = WxApi.getOAuthCodeUrl(mpAccount.getAppid(), redirectUrl, OAuthScope.Base.toString(), state);
				log.info("-------------------------------------preHandle-----<3-4>-------------------url:"+url);	
				HttpUtil.redirectHttpUrl(request, response, url);
				return false;
			}
		}else{
			return true;
		}
		HttpUtil.redirectUrl(request, response, "/error/101.html");
		return false;
	}
	
	
	public String[] getExcludes() {
		return excludes;
	}

	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}

	public String[] getIncludes() {
		return includes;
	}

	public void setIncludes(String[] includes) {
		this.includes = includes;
	}
	
	
}