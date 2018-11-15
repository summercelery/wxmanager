
package springboot.wxcms.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.constant.Constants;
import springboot.core.util.wx.WxUtil;
import springboot.wxapi.vo.Matchrule;
import springboot.wxcms.entity.AccountMenu;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.AccountMenuService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/accountmenu")
public class AccountMenuController {
    @Autowired
    private AccountMenuService accountMenuService;

    @RequestMapping(value = "/list")
    public Result list(AccountMenu accountMenu) {
        List<AccountMenu> menus = accountMenuService.listWxMenus(accountMenu);
        Matchrule matchrule = new Matchrule();
        return Result.ok(WxUtil.prepareMenus(menus, matchrule));
    }

    @RequestMapping(value = "/save")
    public Result save(String menus) {
        JSONArray jsons = JSONArray.parseArray(menus);
        //每次先行删除公众号所有菜单
        accountMenuService.delete(new AccountMenu());
        if (CollectionUtils.isNotEmpty(jsons)) {
            for (int i = 0; i < jsons.size(); i++) {
                JSONObject json = jsons.getJSONObject(i);
                if (null != json) {
                    AccountMenu accountMenu = new AccountMenu();
//					String pid = CommonUtil.getUID();
//					accountMenu.setId(pid);
                    accountMenu.setName(json.getString("name"));
                    accountMenu.setSort(i + 1);
                    accountMenu.setParentId(0L);
                    if (json.containsKey("type")) {
                        accountMenu.setMtype(json.getString("type"));
                        //判断是否设置key
                        if (Constants.MENU_NEED_KEY.contains(json.getString("type"))) {
                            accountMenu.setEventType("fix");
                            accountMenu.setMsgType(json.getString("msgType"));
                            accountMenu.setMsgId(json.getString("msgId"));
                        }
                    }
                    if (json.containsKey("url")) {
                        accountMenu.setUrl(json.getString("url"));
                    }
                    if (json.containsKey("media_id")) {
                        accountMenu.setMsgId(json.getString("media_id"));
                    }
                    accountMenu.setCreateTime(LocalDateTime.now());
                    //保存
                    accountMenuService.add(accountMenu);
                    //判断是否有subbutton
                    if (json.containsKey("sub_button")) {
                        JSONArray buttons = json.getJSONArray("sub_button");
                        if (CollectionUtils.isNotEmpty(buttons)) {
                            Long pid = accountMenu.getId();
                            for (int j = 0; j < buttons.size(); j++) {
                                json = buttons.getJSONObject(j);
                                accountMenu = new AccountMenu();
                                accountMenu.setParentId(pid);
                                accountMenu.setName(json.getString("name"));
                                accountMenu.setSort(j + 1);
                                if (json.containsKey("type")) {
                                    accountMenu.setMtype(json.getString("type"));
                                    //判断是否设置key
                                    if (Constants.MENU_NEED_KEY.contains(json.getString("type"))) {
                                        accountMenu.setEventType("fix");
                                        accountMenu.setMsgType(json.getString("msgType"));
                                        accountMenu.setMsgId(json.getString("msgId"));
                                    }
                                }
                                if (json.containsKey("url")) {
                                    accountMenu.setUrl(json.getString("url"));
                                }
                                if (json.containsKey("media_id")) {
                                    accountMenu.setMsgId(json.getString("media_id"));
                                }
                                accountMenu.setCreateTime(LocalDateTime.now());
                                accountMenuService.add(accountMenu);
                            }
                        }
                    }
                }
            }
        }
        return Result.ok();
    }
}
