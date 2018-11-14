package springboot.wxcms.mapper;


import springboot.wxcms.entity.AccountFans;

public interface AccountFansMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountFans record);

    int insertSelective(AccountFans record);

    AccountFans selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountFans record);

    int updateByPrimaryKeyWithBLOBs(AccountFans record);

    int updateByPrimaryKey(AccountFans record);
}