/*
 * FileName：InitListener.java 
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
package springboot.core.listener;


import springboot.core.spring.SpringContextHolder;
import springboot.core.util.CacheUtils;
import springboot.wxapi.process.WxMemoryCacheClient;
import springboot.wxcms.entity.Account;
import springboot.wxcms.entity.SysConfig;
import springboot.wxcms.service.AccountService;
import springboot.wxcms.service.SysConfigService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class InitListener implements ServletContextListener {
    
    public InitListener() {
    }
    
    public void contextInitialized(ServletContextEvent sce) {
        try {
            //放入公众号
            AccountService accountService = SpringContextHolder.getBean("accountService");
            Account account = accountService.getSingleAccount();
            WxMemoryCacheClient.addMpAccount(account);
            //读取所有缓存
            SysConfigService configService = SpringContextHolder.getBean("sysConfigService");
            List<SysConfig> configList = configService.getConfigList();
            for (SysConfig config : configList) {
                CacheUtils.put(config.getJkey().toUpperCase(), config.getJvalue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
