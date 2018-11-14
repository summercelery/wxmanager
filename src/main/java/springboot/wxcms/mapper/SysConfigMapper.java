package springboot.wxcms.mapper;


import springboot.wxcms.entity.SysConfig;

public interface SysConfigMapper {
    int deleteByPrimaryKey(String jkey);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(String jkey);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);
}