package springboot.wxcms.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.util.wx.WxUtil;
import springboot.wxapi.process.WxMemoryCacheClient;
import springboot.wxcms.entity.Account;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.AccountService;

import java.util.List;

import static springboot.core.constant.sessionConstant.DEFAULT_WECHAT_ACCOUNT_SESSION;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/getById")
    public Result getById(String id) {
        accountService.getById(id);
        return Result.ok();
    }

    @RequestMapping(value = "/listForPage")
    public Result listForPage(Account searchEntity) {
        Account account;
        List<Account> list = accountService.listForPage(searchEntity);
        if (CollectionUtils.isEmpty(list)) {
            return Result.ok();
        }
        //account存入缓存中
        if (null != searchEntity && null != searchEntity.getId()) {
            account = accountService.getById(searchEntity.getId());
        } else {
            account = accountService.getByAccount(WxMemoryCacheClient.getAccount());
        }
        WxMemoryCacheClient.setAccount(account.getAccount());
        return Result.ok(WxUtil.getAccount(list, account.getName()));

    }
}
