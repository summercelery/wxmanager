package springboot.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.core.shiro.CustomerAuthenticationToken;
import springboot.entity.PhoneCode;
import springboot.entity.Result;
import springboot.entity.User;
import springboot.exception.LoginException;
import springboot.service.PhoneCodeService;
import springboot.service.UserService;
import springboot.util.Md5Util;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static springboot.constant.sessionConstant.VALIDATE_CODE_SESSION;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private PhoneCodeService phoneCodeService;


    /**
     * 账号密码登陆
     */
    @PostMapping("login")
    public Result login(
            @NotBlank(message = "账号不能为空") String loginName,
            @NotBlank(message = "密码不能为空") String password,
            @RequestParam(required = false, defaultValue = "true") Boolean rememberMe) {

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
            @RequestParam(required = false) Boolean rememberMe) {

        logger.debug(phone.getPhone() + "正在使用手机验证码登陆........");

        if (result.hasFieldErrors()) {
            return Result.fail(result.getFieldError().getDefaultMessage());
        }

        User user = userService.findUserByPhone(phone.getPhone());
        if (null == user) {
            return Result.fail("此手机号尚未注册");
        }
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(phone.getPhone(), phone.getCode(), rememberMe);
        token.setLoginType("phone");
        Subject currentSubject = SecurityUtils.getSubject();
        try {
            currentSubject.login(token);
        } catch (UnknownAccountException e) {
            return Result.fail("找不到该手机号");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("验证码错误");
        } catch (LoginException e) {
            return Result.fail(e.getMessage());
        }

        return Result.ok(user);
    }


    /**
     * 用户根据手机号注册
     *
     * @param phoneCode
     * @param phoneResult
     * @param password
     * @return
     */
    @PostMapping("phoneRegister")
    public Result phoneRegister(
            @Valid PhoneCode phoneCode, BindingResult phoneResult,
            @NotBlank(message = "密码不能为空") String password) {

        if (phoneResult.hasFieldErrors()) {
            return Result.fail(phoneResult.getFieldError().getDefaultMessage());
        }

        PhoneCode code = phoneCodeService.findPhoneCodeByPhoneAndCodeAndType(phoneCode.getPhone(), phoneCode.getCode(), "register");

        if (null == code) {
            return Result.fail("验证码错误");
        }
        if (System.currentTimeMillis() > code.getExpireTime().getTime()) {
            return Result.fail("验证码已过期");
        }
        if (userService.countUserNumByPhone(phoneCode.getPhone()) >= 1) {
            return Result.fail("该手机号已被注册");
        }
        User user = new User();
        user.setCreateDate(new Date());
        user.setIsDelete(false);
        try {
            //userId作为加密盐
            password = Md5Util.getMd5(password, user.getId());
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


    /**
     * 用户根据邮箱注册
     *
     * @param phoneCode
     * @param phoneResult
     * @param password
     * @return
     */
    @PostMapping("emailRegister")
    public Result emailRegister(
            @Valid PhoneCode phoneCode, BindingResult phoneResult,
            @NotBlank(message = "密码不能为空") String password) {

        if (phoneResult.hasFieldErrors()) {
            return Result.fail(phoneResult.getFieldError().getDefaultMessage());
        }

        PhoneCode code = phoneCodeService.findPhoneCodeByPhoneAndCodeAndType(phoneCode.getPhone(), phoneCode.getCode(), "register");

        if (null == code) {
            return Result.fail("验证码错误");
        }
        if (System.currentTimeMillis() > code.getExpireTime().getTime()) {
            return Result.fail("验证码已过期");
        }
        if (userService.countUserNumByPhone(phoneCode.getPhone()) >= 1) {
            return Result.fail("该手机号已被注册");
        }
        User user = new User();
        user.setCreateDate(new Date());
        user.setIsDelete(false);
        try {
            //userId作为加密盐
            password = Md5Util.getMd5(password, user.getId());
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

    /**
     * 查询手机号是否被注册
     *
     * @param phone
     * @return
     */
    @PostMapping
    public Result isPhoneRegister(String phone) {
        if (userService.countUserNumByPhone(phone) == 0) {
            return Result.ok();
        }
        return Result.fail("该手机号已被注册");
    }


    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {

        String code = defaultKaptcha.createText();
        SecurityUtils.getSubject().getSession().setAttribute(VALIDATE_CODE_SESSION, code);
        BufferedImage image = defaultKaptcha.createImage(code);

        response.setDateHeader("Expires", 0);

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        response.setHeader("Pragma", "no-cache");

        response.setContentType("image/jpeg");

        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresRoles("管理")
    @PostMapping("role")
    public Result role() {
        return Result.ok("只有管理员权限才可访问此接口");
    }


}
