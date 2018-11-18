package springboot.wxcms.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.UserTag;
import springboot.wxcms.mapper.UserTagMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserTagService {

	@Resource
	private UserTagMapper userTagMapper;

	public UserTag getById(String id) {
		return userTagMapper.selectByPrimaryKey(id);
	}

	public List<UserTag> listForPage(UserTag searchEntity) {
		return userTagMapper.getUserTagListByPage(searchEntity);
	}

	public void add(UserTag entity) {
		userTagMapper.insert(entity);
	}

	public void update(UserTag entity) {
		userTagMapper.updateByPrimaryKey(entity);
	}
	public void delete(UserTag entity) {
		userTagMapper.deleteByPrimaryKey(entity);
	}

	public Integer deleteBatchIds(String[] ids) {
		return userTagMapper.deleteBatchIds(ids);
	}


	public Integer getMaxId() {
		return userTagMapper.getMaxId();
	}

}
