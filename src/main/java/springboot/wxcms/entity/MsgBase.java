/*
 * FileName：MsgBase.java 
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package springboot.wxcms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
public class MsgBase extends BaseEntity {
	private String msgtype;//消息类型;
	private String inputcode;//关注者发送的消息
	@JsonIgnore
	private String rule;//规则，目前是 “相等”
	@JsonIgnore
	private Integer enable;//是否可用
	@JsonIgnore
	private Integer readcount;//消息阅读数
	@JsonIgnore
	private Integer favourcount;//消息点赞数

}
