package springboot.wxcms.mapper;


import springboot.wxcms.entity.AccountMenu;

public interface AccountMenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountMenu record);

    int insertSelective(AccountMenu record);

    AccountMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountMenu record);

    int updateByPrimaryKey(AccountMenu record);
}