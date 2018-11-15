
package springboot.wxcms.service;


import org.springframework.stereotype.Service;
import springboot.wxcms.entity.AccountMenu;
import springboot.wxcms.mapper.AccountMenuMapper;

import javax.annotation.Resource;
import java.util.List;


@Service
public class AccountMenuService{

	@Resource
	private AccountMenuMapper accountMenuMapper;

	public AccountMenu getById(String id) {
		return accountMenuMapper.selectByPrimaryKey(id);
	}

	public List<AccountMenu> listWxMenus(AccountMenu entity) {
		return accountMenuMapper.listWxMenus(entity);
	}

	public void add(AccountMenu entity) {
		accountMenuMapper.insert(entity);
	}

	public void update(AccountMenu entity) {
		accountMenuMapper.updateByPrimaryKey(entity);
	}

	public void delete(AccountMenu entity) {
		accountMenuMapper.deleteByPrimaryKey(entity.getId().toString());
	}



}
