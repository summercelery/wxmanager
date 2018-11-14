package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import springboot.wxcms.entity.SysUser;
import springboot.wxcms.mapper.SysUserMapper;

import javax.annotation.Resource;


@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    public SysUser getSysUser(SysUser sysUser) {
        return sysUserMapper.getSysUser(sysUser);
    }

    public SysUser getSysUserById(String userId) {
        return sysUserMapper.getSysUserById(userId);
    }

    public int updateLoginPwd(SysUser sysUser) {
        return sysUserMapper.updateLoginPwd(sysUser);
    }

    public int createUser(SysUser sysUser) {
        return sysUserMapper.insertSelective(sysUser);
    }

    public SysUser findUserByEmail(String email) {
        return sysUserMapper.findUserByEmail(email);
    }

    public SysUser findUserByPhone(String phone) {
        return sysUserMapper.findUserByPhone(phone);
    }

    public int countUserNumByPhone(String phone) {
        return sysUserMapper.countUserNumByPhone(phone);
    }

}
