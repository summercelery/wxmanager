/*
 * FileName：MaterialCount.java 
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
package springboot.wxapi.vo;

import lombok.Data;

/**
 * 素材总数
 */
@Data
public class MaterialCount {
	private Integer voiceCount;//语音总数量
	private Integer videoCount;//视频总数
	private Integer imageCount;//图片总数
	private Integer newsCount;//图文总数
}
