
package springboot.wxcms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.wxcms.entity.AccountFans;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.AccountFansService;

import java.util.List;

@RestController
@RequestMapping("/accountfans")
public class AccountFansController {

    @Autowired
    private AccountFansService accountFansService;

    @RequestMapping(value = "/info")
    public Result getById(String id) {
        AccountFans fans = accountFansService.getById(id);
        return Result.ok(fans);
    }

    @RequestMapping(value = "/list")
    public Result list(AccountFans searchEntity) {
        List<AccountFans> fansList = accountFansService.getFansListByPage(searchEntity);
        return Result.ok(fansList);
    }

}
