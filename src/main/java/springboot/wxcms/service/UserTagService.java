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

	public UserTag getById(Integer id) {
		return userTagMapper.getById(id);
	}

	public List<UserTag> listForPage(UserTag searchEntity) {
		return userTagMapper.getUserTagListByPage(searchEntity);
	}

	public void add(UserTag entity) {
		userTagMapper.add(entity);
	}

	public void update(UserTag entity) {
		userTagMapper.update(entity);
	}
	public void delete(UserTag entity) {
		userTagMapper.delete(entity);
	}

	public Integer deleteBatchIds(String[] ids) {
		return userTagMapper.deleteBatchIds(ids);
	}


	public Integer getMaxId() {
		return userTagMapper.getMaxId();
	}

}
