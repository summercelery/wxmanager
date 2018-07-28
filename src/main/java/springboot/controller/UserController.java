package springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.shiro.CustomerAuthenticationToken;
import springboot.entity.PhoneCode;
import springboot.entity.Result;
import springboot.entity.User;
import springboot.service.UserService;
import springboot.util.Md5Util;
import springboot.util.UUIDUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 账号密码登陆
     */
    @PostMapping("login")
    public Result login(
            @NotBlank(message = "账号不能为空")String LoginName,
            @NotBlank(message = "密码不能为空")String password,
            @RequestParam(required = false,defaultValue = "true") Boolean rememberMe)
    {

        logger.debug(LoginName+"正在登陆........");

        CustomerAuthenticationToken token = new CustomerAuthenticationToken(LoginName, password, rememberMe);
        token.setLoginType("loginName");
        Subject currentSubject = SecurityUtils.getSubject();
        try {
            currentSubject.login(token);
        } catch (UnknownAccountException e) {
            return Result.fail("找不到该用户名");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("密码错误");
        }

        return Result.ok(currentSubject.getPrincipal());
    }

    /**
     * 手机验证码登陆
     * @param phone
     * @param result
     * @param rememberMe
     * @return
     */
    @PostMapping("phoneLogin")
    public Result phoneLogin(@Valid PhoneCode phone , BindingResult result , @RequestParam(required = false) Boolean rememberMe){

        logger.debug(phone.getPhone()+"正在使用验证码登陆........");

        if(result.hasFieldErrors()){
            return Result.fail(result.getFieldError().getDefaultMessage());
        }

        User user = userService.findUserByPhone(phone.getPhone());
        if(null ==user ){
            return Result.fail("此手机号尚未注册");
        }
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(phone.getPhone(),phone.getCode(),rememberMe);
        token.setLoginType("phone");
        Subject currentSubject  = SecurityUtils.getSubject();
        try{
            currentSubject.login(token);
        }catch (UnknownAccountException e){
            return Result.fail("找不到该手机号");
        }catch (IncorrectCredentialsException e){
            return Result.fail("验证码错误");
        }catch (LoginException e){
            return Result.fail(e.getMessage());
        }

        return Result.ok(user);
    }


    /**
     * 用户注册
     * @param phoneCode
     * @param phoneResult
     * @param user
     * @param userResult
     * @return
     */
    @PostMapping("register")
    public Result register(@Valid PhoneCode phoneCode,BindingResult phoneResult,@Valid User user, BindingResult userResult) {

        if (phoneResult.hasFieldErrors()) {
            return Result.fail(phoneResult.getFieldError().getDefaultMessage());
        }
        if (userResult.hasFieldErrors()) {
            return Result.fail(userResult.getFieldError().getDefaultMessage());
        }

        PhoneCode code = phoneCodeService.findPhoneCodeByPhoneAndCodeAndType(phoneCode.getPhone(),phoneCode.getCode(),"register");

        if(null == code){
            return Result.fail("验证码错误");
        }
        if(System.currentTimeMillis() > code.getExpireTime().getTime()){
            return Result.fail("验证码已过期");
        }

        if (userService.countUserNumByLoginName(user.getLoginName()) >= 1) {
            return Result.fail("该账号已被注册");
        }
        if (userService.countUserNumByPhone(user.getPhone()) >= 1){
            return Result.fail("该手机号已被注册");
        }

        user.setId(UUIDUtil.getUUID());
        user.setCreateDate(new Date());
        user.setIsDelete(false);
        String password = null;
        try {
            //userId作为加密盐
            password = Md5Util.getMd5(user.getPassword(),user.getId());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Result.fail("服务器异常");
        }
        user.setPassword(password);
        if (1 == userService.createUser(user)) {
            return Result.ok(user);
        }

        return Result.fail("服务器异常");

    }

    @RequiresRoles("管理")
    @PostMapping("role")
    public Result role() {
        return Result.ok("只有管理员权限才可访问此接口");
    }





}
