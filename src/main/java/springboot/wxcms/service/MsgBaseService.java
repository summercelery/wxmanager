
package springboot.wxcms.service;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.mapper.MsgBaseMapper;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MsgBaseService {

	@Resource
	private MsgBaseMapper msgBaseMapper;

	public MsgBase getById(String id){
		return msgBaseMapper.selectByPrimaryKey(id);
	}

	public List<MsgBase> listForPage(MsgBase searchEntity){
		PageHelper.startPage(searchEntity.getPageNum(),searchEntity.getPageSize());
		return msgBaseMapper.listForPage(searchEntity);
	}

	public void add(MsgBase entity){
		msgBaseMapper.insert(entity);
	}

	public void update(MsgBase entity){
		msgBaseMapper.updateByPrimaryKey(entity);
	}

	public void delete(MsgBase entity){
		msgBaseMapper.deleteByPrimaryKey(entity.getId());
	}



}
