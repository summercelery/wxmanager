
package springboot.wxcms.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springboot.wxapi.process.MsgType;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.entity.MsgText;
import springboot.wxcms.mapper.MsgTextMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class MsgTextService {

    @Resource
    private MsgTextMapper msgTextMapper;

    @Resource
    private MsgBaseMapper msgBaseMapper;

    public MsgText getById(String id) {
        return msgTextMapper.selectByPrimaryKey(id);
    }

    public List<MsgText> getMsgTextByPage(MsgText searchEntity) {
        return msgTextMapper.getMsgTextByPage(searchEntity);
    }

    public void add(MsgText entity) {
        MsgBase base = new MsgBase();
        base.setInputcode(entity.getInputcode());
        base.setCreateTime(LocalDateTime.now());
        base.setMsgtype(MsgType.Text.toString());
        MsgBaseMapper.add(base);
        entity.setBaseId(base.getId());
        msgTextMapper.add(entity);
    }

    public void update(MsgText entity) {
        MsgBase base = MsgBaseMapper.getById(entity.getBaseId().toString());
        base.setInputcode(entity.getInputcode());
        MsgBaseMapper.updateInputcode(base);
        msgTextMapper.update(entity);
    }

    public void delete(String baseIds) {
        String[] ids = StringUtils.split(baseIds, ",");
        for (String id : ids) {
            MsgBase base = new MsgBase();
            MsgText msgText = new MsgText();
            base.setId(id);
            msgText.setBaseId(Long.valueOf(id));
            msgTextMapper.delete(msgText);
            MsgBaseMapper.delete(base);
        }
    }

    //根据用户发送的文本消息，随机获取一条文本消息
    public MsgText getRandomMsg(String inputCode) {
        return msgTextMapper.getRandomMsg(inputCode);
    }

    public MsgText getRandomMsg2() {
        return msgTextMapper.getRandomMsg2();
    }

    /* (non-Javadoc)
     * @see com.wxmp.wxcms.service.MsgTextService#getByBaseId(java.lang.String)
     */
    public MsgText getByBaseId(String baseid) {
        // TODO Auto-generated method stub
        return msgTextMapper.getByBaseId(baseid);
    }
}
