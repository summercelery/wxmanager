package springboot.wxcms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import springboot.wxcms.entity.AccountMenuGroup;

import java.util.List;

public interface AccountMenuGroupMapper {


    @ResultMap("BaseResultMap")
    @Select("select * from account_menu_group order by id")
    List<AccountMenuGroup> list(AccountMenuGroup searchEntity);

    @Select("SELECT COUNT(*) FROM account_menu_group")
    Integer getTotalItemsCount(AccountMenuGroup searchEntity);

    @Select("SELECT * FROM account_menu_group ORDER BY create_time")
    List<AccountMenuGroup> getGroupListByPage(AccountMenuGroup searchEntity);


    @Update("UPDATE account_menu_group SET ENABLE = 0")
    void updateMenuGroupDisable();

    @Update("UPDATE account_menu_group SET ENABLE = 1 WHERE ID = #{id}")
    void updateMenuGroupEnable(String id);

    @Delete("DELETE FROM account_menu WHERE GID = #{id}")
    void deleteAllMenu(String id);


    /**
     * 删除菜单组下的菜单
     * @param id
     */
    @Delete("DELETE FROM account_menu WHERE GID = #{id}")
    void deleteMenuByGId(long id);

    int deleteByPrimaryKey(String id);

    int insert(AccountMenuGroup record);

    int insertSelective(AccountMenuGroup record);

    AccountMenuGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountMenuGroup record);

    int updateByPrimaryKey(AccountMenuGroup record);
}
