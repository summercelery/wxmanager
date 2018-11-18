
package springboot.wxcms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.exception.BusinessException;
import springboot.wxcms.entity.Result;
import springboot.wxcms.entity.TplMsgText;
import springboot.wxcms.service.TplMsgTextService;

import java.util.List;


@RestController
@RequestMapping("/tplmsgtext")
public class TplMsgTextController {

	@Autowired
	private TplMsgTextService tplMsgTextService;
	

	@RequestMapping(value = "/getById")
	public Result getById(String id){
		TplMsgText text = tplMsgTextService.getById(id);
		return Result.ok(text);
	}

	@RequestMapping(value = "/list")
	public  Result list(TplMsgText searchEntity) throws BusinessException {
		List<TplMsgText> pageList = tplMsgTextService.getTplMsgTextByPage(searchEntity);
		return Result.ok(pageList);
	}


	/**
	 * 修改/添加
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/updateText")
	public Result updateText(TplMsgText entity){
		if (entity.getId() != null) {
			tplMsgTextService.update(entity);
			//更新成功
			return Result.ok();
		} else {
			//添加成功
			tplMsgTextService.add(entity);
			return Result.ok();
		}
	}

	@RequestMapping(value = "/deleteById")
	public Result deleteById(String baseId) {
		tplMsgTextService.delete(baseId);
		return Result.ok();
	}

}
