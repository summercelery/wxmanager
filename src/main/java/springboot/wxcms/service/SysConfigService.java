/*
 * FileName：SysConfigServiceImpl.java 
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
package springboot.wxcms.service;


import org.springframework.stereotype.Service;
import springboot.wxcms.entity.SysConfig;
import springboot.wxcms.mapper.SysConfigMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysConfigService")
public class SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;


    public List<SysConfig> getConfigList() {
        return sysConfigMapper.getConfigList();
    }

    public Map<String, String> getSysConfigToMap() {
        List<SysConfig> allList = this.getConfigList();
        Map<String,String> map = new HashMap<>();
        for (SysConfig config : allList) {
            map.put(config.getJkey(),config.getJvalue());
        }
        return map;
    }

    public String getValue(String key) {
        return sysConfigMapper.getValue(key);
    }

    public void update(Map<String, String> params, HttpServletRequest request) {
        for(Map.Entry entry : params.entrySet()){
            sysConfigMapper.update((String)entry.getKey(),(String)entry.getValue());
            }
    }
    public String getMysqlVsesion() {
        return sysConfigMapper.getMysqlVsesion();
    }
}
