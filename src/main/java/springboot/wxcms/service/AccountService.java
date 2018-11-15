package springboot.wxcms.service;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.Account;
import springboot.wxcms.mapper.AccountMapper;

import javax.annotation.Resource;
import java.util.List;

@Service("accountService")
public class AccountService  {
    
    @Resource
    private AccountMapper accountMapper;
    
    public Account getById(String id) {
        return accountMapper.selectByPrimaryKey(id);
    }
    
    public Account getByAccount(String account) {
        return accountMapper.findByAccount(account);
    }
    
    public List<Account> listForPage(Account searchEntity) {
        PageHelper.startPage(searchEntity.getPageNum(), searchEntity.getPageSize());
        return accountMapper.findAll();
    }
    
    public void add(Account entity) {
        accountMapper.insert(entity);
    }
    
    public void update(Account entity) {
        accountMapper.updateByPrimaryKey(entity);
    }
    
    public void delete(Account entity) {
        accountMapper.deleteByPrimaryKey(entity.getId());
    }
    
    public Account getSingleAccount() {
        return accountMapper.getSingleAccount();
    }
    
}
