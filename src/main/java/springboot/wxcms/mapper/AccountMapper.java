package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.Account;

import java.util.List;

public interface AccountMapper {

    @ResultMap("BaseResultMap")
    @Select("select * from account order by create_time")
    List<Account> findAll();

    @ResultMap("BaseResultMap")
    @Select("select * from account where account = #{account} limit 1")
    Account findByAccount(String account);

    @ResultMap("BaseResultMap")
    @Select("select * from account limit 1")
    Account getSingleAccount();


    int deleteByPrimaryKey(String id);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}