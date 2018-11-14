package springboot.wxcms.service;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.Account;
import javax.annotation.Resource;
import java.util.List;

@Service("accountService")
public class AccountService  {
    
    @Resource
    private AccountDao entityDao;
    
    public Account getById(Long id) {
        return entityDao.getById(id);
    }
    
    public Account getByAccount(String account) {
        return entityDao.getByAccount(account);
    }
    
    public List<Account> listForPage(Account searchEntity) {
        return entityDao.listForPage(searchEntity);
    }
    
    public void add(Account entity) {
        entityDao.add(entity);
    }
    
    public void update(Account entity) {
        entityDao.update(entity);
    }
    
    public void delete(Account entity) {
        entityDao.delete(entity);
    }
    
    public Account getSingleAccount() {
        return entityDao.getSingleAccount();
    }
    
}
