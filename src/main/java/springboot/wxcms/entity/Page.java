/*
 * FileName：Page.java 
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

import lombok.Data;
import springboot.wxapi.process.WxMemoryCacheClient;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 分页参数实体类
 */
@Data
public class Page implements Serializable {

    @Transient
    private Integer pageNum = 1;// 当前页索引

    @Transient
    private Integer pageSize = 20;// 每页显示的数据条数

    @Transient
    private Long total = 0L;// 总条数

    @Transient
    private Integer pages = 1;// 总页数
    
    // private String sort;// 排序字段
    //
    // private String order;// 排序方式 ASC DESC
    private String account = WxMemoryCacheClient.getAccount();


}
