
package springboot.wxcms.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springboot.wxapi.process.MsgType;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.entity.TplMsgText;
import springboot.wxcms.mapper.TplMsgTextMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class TplMsgTextService{

	@Resource
	private TplMsgTextMapper tplMsgTextMapper;

	@Resource
	private MsgBaseMapper msgBaseMapper;

	public TplMsgText getById(String id){
		return tplMsgTextMapper.getById(id);
	}

	public List<TplMsgText> getTplMsgTextByPage(TplMsgText searchEntity){
		return tplMsgTextMapper.getTplMsgTextByPage(searchEntity);
	}

	public void add(TplMsgText entity){
		MsgBase base = new MsgBase();
		base.setInputcode(entity.getInputcode());
		base.setCreateTime(new Date());
		base.setMsgtype(MsgType.Text.toString());
		msgBaseMapper.add(base);
		
		entity.setBaseId(base.getId());
		tplMsgTextMapper.insert(entity);
	}

	public void update(TplMsgText entity){
		MsgBase base = msgBaseMapper.getById(entity.getBaseId().toString());
		base.setInputcode(entity.getInputcode());
		msgBaseMapper.updateInputcode(base);
		tplMsgTextMapper.updateByPrimaryKey(entity);
	}

    public void delete(String baseIds) {
        String[] ids = StringUtils.split(baseIds, ",");
        for (String id : ids) {
            MsgBase base = new MsgBase();
            TplMsgText tplMsgText = new TplMsgText();
            base.setId(id);
            tplMsgText.setBaseId(Long.valueOf(id));
			tplMsgTextMapper.deleteByPrimaryKey(tplMsgText);
			msgBaseMapper.delete(base);
        }
    }

	public TplMsgText getByBaseId(String baseid) {
		return tplMsgTextMapper.getByBaseId(baseid);
	}
}
