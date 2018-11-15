
package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import springboot.wxcms.entity.MsgBase;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MsgBaseService {

	@Resource
	private MsgBaseMapper msgBaseMapper;

	public MsgBase getById(String id){
		return msgBaseMapper.getById(id);
	}

	public List<MsgBase> listForPage(MsgBase searchEntity){
		return msgBaseMapper.listForPage(searchEntity);
	}

	public void add(MsgBase entity){
		msgBaseMapper.add(entity);
	}

	public void update(MsgBase entity){
		msgBaseMapper.update(entity);
	}

	public void delete(MsgBase entity){
		msgBaseMapper.delete(entity);
	}



}
