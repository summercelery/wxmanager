
package springboot.core.listener;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springboot.core.spring.SpringContextHolder;
import springboot.core.util.CacheUtils;
import springboot.wxapi.process.WxMemoryCacheClient;
import springboot.wxcms.entity.Account;
import springboot.wxcms.entity.SysConfig;
import springboot.wxcms.service.AccountService;
import springboot.wxcms.service.SysConfigService;

import java.util.List;


@Component
public class InitListener implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        try {
            //放入公众号
            AccountService accountService = SpringContextHolder.getBean("accountService");
            Account accounts = accountService.getSingleAccount();
            WxMemoryCacheClient.addMpAccount(accounts);
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
}
