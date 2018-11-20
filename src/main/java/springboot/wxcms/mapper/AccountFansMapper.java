package springboot.wxcms.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import springboot.wxcms.entity.AccountFans;

import java.util.List;

public interface AccountFansMapper {


    @ResultMap("BaseResultMap")
    @Select("select * from account_fans where open_id = #{openId}")
    AccountFans getByOpenId(String openId);

    List<AccountFans> getFansListByPage(AccountFans searchEntity);


    @ResultMap("BaseResultMap")
    @Select("select * from account_fans order by create_time")
    List<AccountFans> list(AccountFans searchEntity);

    @Select("select count(1) from account_fans")
    Integer getTotalItemsCount(AccountFans searchEntity);

    @ResultMap("BaseResultMap")
    @Select("select * from account_fans order by create_time DESC LIMIT 0 , 1")
    AccountFans getLastOpenId();


    @Delete("delete from account_fans where open_id = #{openId}")
    void deleteByOpenId(String openId);

    void addList(List<AccountFans> list);


    /**
     * 根据多个openId查看粉丝列表
     * @param openIds
     * @return List
     */
    public List<AccountFans> getFansByOpenIdListByPage(List<AccountFans> openIds);




















    int deleteByPrimaryKey(String id);

    int insert(AccountFans record);

    int insertSelective(AccountFans record);

    AccountFans selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountFans record);

    int updateByPrimaryKeyWithBLOBs(AccountFans record);

    int updateByPrimaryKey(AccountFans record);
}