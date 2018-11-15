
package springboot.wxcms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.wxcms.entity.MsgText;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.MsgBaseService;
import springboot.wxcms.service.MsgTextService;

import java.util.List;


@RestController
@RequestMapping("/msgtext")
public class MsgTextCtrl  {

    @Autowired
    private MsgTextService msgTextService;

    @Autowired
    private MsgBaseService msgBaseService;

    @RequestMapping(value = "/getById")
    public Result getById(String id) {
        MsgText text = msgTextService.getById(id);
        return Result.success(text);
    }

    @ResponseBody
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
            return Result.failure("关键词重复");
        }
        if (entity.getId() != null) {
            msgTextService.update(entity);
            // 更新成功
            return Result.updateSuccess();
        } else {
            // 添加成功
            msgTextService.add(entity);
            return Result.saveSuccess();
        }
    }

    @RequestMapping(value = "/deleteById")
    public Result deleteById(String baseId) {
        msgTextService.delete(baseId);
        return Result.deleteSuccess();
    }

}
