package springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.shiro.CustomerAuthenticationToken;
import springboot.entity.PhoneCode;
import springboot.entity.Result;
import springboot.exception.LoginException;
import springboot.service.UserService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("login")
@Validated
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private UserService userService;

    /**
     * 账号密码登陆
     */
    @PostMapping("loginName")
    public Result login(
            @NotBlank(message = "账号不能为空") String loginName,
            @NotBlank(message = "密码不能为空") String password,
            @RequestParam(required = false, defaultValue = "false") Boolean rememberMe) {

        logger.debug(loginName + "正在登陆........");

        CustomerAuthenticationToken token = new CustomerAuthenticationToken(loginName, password, rememberMe);
        token.setLoginType("loginName");
        Subject currentSubject = SecurityUtils.getSubject();
        try {
            currentSubject.login(token);
        } catch (UnknownAccountException e) {
            return Result.fail("找不到该账户");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("密码错误");
        }
        logger.debug(loginName + "登陆成功........");

        return Result.ok(currentSubject.getPrincipal());
    }


    /**
     * 手机验证码登陆
     *
     * @param phone
     * @param result
     * @param rememberMe
     * @return
     */
    @PostMapping("phoneLogin")
    public Result phoneLogin(
            @Valid PhoneCode phone, BindingResult result,
            @RequestParam(required = false, defaultValue = "false") Boolean rememberMe) {

        if (result.hasFieldErrors()) {
            return Result.fail(result.getFieldError().getDefaultMessage());
        }
        logger.debug(phone.getPhone() + "正在使用手机验证码登陆........");

        CustomerAuthenticationToken token = new CustomerAuthenticationToken(phone.getPhone(), phone.getCode(), rememberMe);
        token.setLoginType("phone");
        Subject currentSubject = SecurityUtils.getSubject();
        try {
            currentSubject.login(token);
            logger.debug(phone.getPhone() + "已使用手机验证码登陆成功........");
        } catch (UnknownAccountException e) {
            return Result.fail("此手机号尚未注册");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("验证码错误");
        } catch (LoginException e) {
            return Result.fail(e.getMessage());
        }
        logger.debug(phone.getPhone() + "正在登陆........");

        return Result.ok(currentSubject.getPrincipal());
    }


}
