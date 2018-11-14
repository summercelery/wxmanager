package springboot.wxcms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springboot.core.anno.Phone;
import springboot.core.core.redis.JedisService;
import springboot.wxcms.entity.Email;
import springboot.wxcms.entity.PhoneCode;
import springboot.wxcms.entity.Result;
import springboot.wxcms.entity.User;
import springboot.wxcms.service.EmailService;
import springboot.wxcms.service.PhoneCodeService;
import springboot.wxcms.service.UserService;
import springboot.core.util.MapUtil;
import springboot.core.util.Md5Util;
import springboot.core.util.UUIDUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import static springboot.core.constant.RedisConstant.REDIS_EMAIL_EXPIRE;
import static springboot.core.constant.RedisConstant.REDIS_EMAIL_USER_DB;
import static springboot.core.constant.sessionConstant.REGISTER_PHONE_CODE_ID_SESSION;

@RestController
@RequestMapping("register")
@Validated//开启hibernate validator对参数的校验
@Slf4j
public class registerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PhoneCodeService phoneCodeService;

    @Autowired
    private EmailService emailServer;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sendAddress;



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
        Session session = SecurityUtils.getSubject().getSession();
        if (null != session.getAttribute(REGISTER_PHONE_CODE_ID_SESSION)) {

            PhoneCode code = phoneCodeService.findPhoneCodeById(session.getAttribute(REGISTER_PHONE_CODE_ID_SESSION).toString());
            if (!phoneCode.getPhone().equals(code.getPhone())) {
                return Result.fail("请输入正确的手机号");
            }

            if (code.getExpireTime().before(new Date())) {
                return Result.fail("验证码已过期");
            }
            if (!code.getCode().equals(phoneCode.getCode())) {
                return Result.fail("验证码错误");
            }
            if (userService.countUserNumByPhone(phoneCode.getPhone()) >= 1) {
                return Result.fail("该手机号已被注册");
            }
            User user = new User();
            user.setCreateDate(new Date());
            user.setIsDelete(false);
            user.setPhone(phoneCode.getPhone());
            user.setName(phoneCode.getPhone());
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

        }
        return Result.fail("请获取短信");

    }


    /**
     * 用户根据邮箱注册
     */
    @PostMapping("emailRegister")
    public Result emailRegister(
            @NotBlank(message = "邮箱不能为空") @javax.validation.constraints.Email(message = "邮箱格式不正确") String emailAddress,
            @NotBlank(message = "密码不能为空") @Length(min = 8,max = 30,message = "请输入正确位数的密码") String password) throws NoSuchAlgorithmException, JsonProcessingException {

        if (emailServer.countNumByEmail(emailAddress) > 0) {
            return Result.fail("该邮箱已被注册");
        }
        //激活参数与redis主键
        String uuid = UUIDUtil.getUUID();

        User user = new User();
        password = Md5Util.getMd5(password, user.getId());
        user.setPassword(password);
        user.setEmail(emailAddress);
        user.setName(emailAddress);
        jedisService.setHash(uuid, MapUtil.objectToMap(user), REDIS_EMAIL_USER_DB, REDIS_EMAIL_EXPIRE);

        Email email = new Email();
        email.setTitle("注册验证");
        email.setSendAddress(sendAddress);
        email.setReceiveAddress(emailAddress);

        Context context = new Context();
        context.setVariable("id", uuid);
        String htmlContext = templateEngine.process("template/registerEmailTemplate", context);
        email.setContent(htmlContext);
        emailServer.createEmail(email);
        emailServer.sendHtmlMail(email);

        return Result.ok();
    }


    /**
     * 激活邮件注册的用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("active/{id}")
    public Result activeEmail(@NotBlank @PathVariable String id) throws Exception {


        Map<String, String> map = jedisService.getAllHash(id, REDIS_EMAIL_USER_DB);
        if (null == map) {
            return Result.fail("激活链接已过期或错误，请在24小时内激活");
        }
        User user = MapUtil.mapToObject(map, User.class);
        userService.createUser(user);
        jedisService.deleteKey(id,REDIS_EMAIL_USER_DB);
        return Result.ok();
    }


    /**
     * 查询手机号是否被注册
     *
     * @param phone
     * @return
     */
    @PostMapping("isPhoneRegister")
    public Result isPhoneRegister(@NotBlank @Phone String phone) {
        if (userService.countUserNumByPhone(phone) == 0) {
            return Result.ok();
        }
        return Result.fail("该手机号已被注册");
    }


}
