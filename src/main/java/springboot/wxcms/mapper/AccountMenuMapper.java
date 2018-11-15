package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.AccountMenu;

import java.util.List;

public interface AccountMenuMapper {

    @ResultMap("BaseResultMap")
    @Select("SELECT * FROM account_menu WHERE account=#{account} ORDER BY parent_id, sort")
    List<AccountMenu> listWxMenus(AccountMenu entity);


    int deleteByPrimaryKey(String id);

    int insert(AccountMenu record);

    int insertSelective(AccountMenu record);

    AccountMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountMenu record);

    int updateByPrimaryKey(AccountMenu record);
}