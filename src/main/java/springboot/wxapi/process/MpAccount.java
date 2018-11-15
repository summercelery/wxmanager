
package springboot.wxapi.process;

import lombok.Data;
import springboot.wxcms.entity.Page;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 微信公众号信息
 */
@Data
//jpa用来标志父类的注解
@MappedSuperclass
public class MpAccount extends Page{

	private String account;//账号
	private String appid;//appid
	private String appsecret;//appsecret
	private String url;//验证时用的url
	private String token;//token
	//ext
	private Integer msgcount;//自动回复消息条数;默认是5条

	public Integer getMsgcount() {
		if(msgcount == null)
			msgcount = 5;//默认5条
		return msgcount;
	}

}
