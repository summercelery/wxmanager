/*
 * FileName：AccountFansServiceImpl.java 
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package springboot.wxcms.service;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.AccountFans;
import springboot.wxcms.mapper.AccountFansMapper;

import javax.annotation.Resource;
import java.util.List;


@Service
public class AccountFansService{

	@Resource
	private AccountFansMapper accountFansMapper;

	public AccountFans getById(String id){
		return accountFansMapper.selectByPrimaryKey(id);
	}

	public AccountFans getByOpenId(String openId){
		return accountFansMapper.getByOpenId(openId);
	}
	
	public List<AccountFans> list(AccountFans searchEntity){
		return accountFansMapper.list(searchEntity);
	}

	public List<AccountFans> getFansListByPage(AccountFans searchEntity){
		PageHelper.startPage(searchEntity.getPageNum(),searchEntity.getPageSize());
		return accountFansMapper.getFansListByPage(searchEntity);
	}
	
	public AccountFans getLastOpenId(){
		return accountFansMapper.getLastOpenId();
	}
	
	public void sync(AccountFans searchEntity){
		AccountFans lastFans = accountFansMapper.getLastOpenId();
		String lastOpenId = "";
		if(lastFans != null){
			lastOpenId = lastFans.getOpenId();
		}
	}
	public void add(AccountFans entity){
		accountFansMapper.insert(entity);
	}

	public void update(AccountFans entity){
		accountFansMapper.updateByPrimaryKey(entity);
	}

	public void delete(AccountFans entity){
		accountFansMapper.deleteByPrimaryKey(entity.getId());
	}

	public void deleteByOpenId(String openId){
		accountFansMapper.deleteByOpenId(openId);
	}

	public List<AccountFans> getFansByOpenIdListByPage(List<AccountFans> openIds) {
		return accountFansMapper.getFansByOpenIdListByPage(openIds);
	}
}
