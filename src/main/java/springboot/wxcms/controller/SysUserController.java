package springboot.wxcms.controller;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.core.shiro.CustomerAuthenticationToken;
import springboot.core.util.Md5Util;
import springboot.wxcms.entity.Result;
import springboot.wxcms.entity.SysUser;
import springboot.wxcms.service.AccountService;
import springboot.wxcms.service.SysUserService;


@RestController
@RequestMapping("user")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/login")
    public Result login(SysUser user, Boolean rememberMe) {

        log.debug(user.getLoginName() + "正在登陆........");
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(user.getLoginName(), user.getPassword(), rememberMe);
        token.setLoginType("loginName");
        Subject currentSubject = SecurityUtils.getSubject();
        currentSubject.getSession().setAttribute("DEFAULT_WECHAT_ACCOUNT_SESSION", accountService.getSingleAccount());
        try {
            currentSubject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            return Result.fail("用户名或密码错误");
        }
        log.debug(user.getLoginName() + "登陆成功........");
        return Result.ok(currentSubject.getPrincipal());
    }

    @PostMapping(value = "/updatepwd")
    public Result updatepwd(String newPassword) {
        Subject currentSubject = SecurityUtils.getSubject();
        SysUser sysUse = (SysUser) currentSubject.getPrincipal();
        sysUse.setPassword(Md5Util.getMd5(newPassword, sysUse.getId()));
        this.sysUserService.updateLoginPwd(sysUse);
        currentSubject.logout();
        return Result.ok();
    }

    /**
     * 用户退出
     */
    @PostMapping("logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }
}
