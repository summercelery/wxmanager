
package springboot.wxcms.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.exception.BusinessException;
import springboot.wxcms.entity.MsgText;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.MsgBaseService;
import springboot.wxcms.service.MsgTextService;

import java.util.List;


@RestController
@RequestMapping("/msgtext")
public class MsgTextController {

    @Autowired
    private MsgTextService msgTextService;

    @RequestMapping(value = "/getById")
    public Result getById(String id) {
        MsgText text = msgTextService.getById(id);
        return Result.ok(text);
    }

    @RequestMapping(value = "/list")
    public Result list(MsgText searchEntity) throws BusinessException {
        List<MsgText> pageList = msgTextService.getMsgTextByPage(searchEntity);

        return Result.ok(pageList);
    }

    /**
     * 修改/添加
     * 
     * @param entity
     * @return
     */
    @RequestMapping(value = "/updateText")
    public Result updateText(MsgText entity) {
        // 文本消息的关键词需要保证唯一性
        MsgText msgText = msgTextService.getRandomMsg(entity.getInputcode());
        if (msgText != null) {
            return Result.fail("关键词重复");
        }
        if (entity.getId() != null) {
            msgTextService.update(entity);
            // 更新成功
            return Result.ok();
        } else {
            // 添加成功
            msgTextService.add(entity);
            return Result.ok();
        }
    }

    @RequestMapping(value = "/deleteById")
    public Result deleteById(String baseId) {
        msgTextService.delete(baseId);
        return Result.ok();
    }

}
