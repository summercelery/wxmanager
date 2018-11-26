
package springboot.wxcms.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "msg_text")
public class MsgText extends MsgBase{
	private String title;//消息标题
	private String content;//消息内容
	private String baseId;//消息主表id

}
