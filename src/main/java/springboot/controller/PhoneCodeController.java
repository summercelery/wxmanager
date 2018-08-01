package springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.anno.Phone;
import springboot.core.redis.JedisService;
import springboot.entity.PhoneCode;
import springboot.entity.Result;
import springboot.service.PhoneCodeService;
import springboot.service.UserService;
import springboot.util.MapUtil;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Calendar;

import static springboot.constant.RedisConstant.*;
import static springboot.constant.sessionConstant.PHONE_CODE_ID_SESSION;
import static springboot.constant.sessionConstant.VALIDATE_CODE_SESSION;

@RestController
@RequestMapping("phonecode")
public class PhoneCodeController {

    private static Logger logger = LoggerFactory.getLogger(PhoneCodeController.class);

    //短信积压数
    public static Long phoneOverStock = 0L;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhoneCodeService phoneCodeService;


    /**
     * 获取短信验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("getCode")
    public Result getCode(
            @Phone @NotBlank(message = "手机号不能为空") String phone,
            @NotBlank(message = "发送类型不能为空") String type,
            @NotBlank(message = "验证码不能为空") String validateCode
    ) throws IOException {
        Subject currentSubject = SecurityUtils.getSubject();

        if(!validateCode.equals(currentSubject.getSession().getAttribute(VALIDATE_CODE_SESSION))){
            return Result.fail("验证码错误");
        }

        Integer userNum = userService.countUserNumByPhone(phone);
        if ("login".equals(type) && userNum < 1) {
            return Result.fail("该手机号尚未注册");
        } else if ("register".equals(type) && userNum >= 1) {
            return Result.fail("该手机号已注册");
        } else if (!"login".equals(type) && !"register".equals(type)) {
            return Result.fail("未定义的短信类型");
        }
        if (jedisService.existKey(type + phone, REDIS_PHONE_COUNT_DOWN_DB)) {
            return Result.fail("60秒内请不要重复提交");
        }
        PhoneCode phoneCode = phoneCodeService.createAndSendPhoneCode(phone,type);
        //将成功发送的短信id放置入session中
        currentSubject.getSession().setAttribute(PHONE_CODE_ID_SESSION,phoneCode.getId());

        return Result.ok();

    }

    /**
     * 设置短信定时发送
     *
     * @param message
     * @param phone
     * @return
     */
    @PostMapping("setColck")
    public Result setClockPhone(String message, String phone) {
        PhoneCode phoneCode = new PhoneCode();
        phoneCode.setPhone(phone);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        phoneCode.setClockTime(calendar.getTime());
        phoneCode.setMessage(message);
        phoneCode.setType("ad");
        phoneCodeService.createPhoneCode(phoneCode);

        jedisService.setHash(phoneCode.getId(), MapUtil.objectToMap(phoneCode), REDIS_CLOCK_PHONE_DB, REDIS_PHONE_EXPIRE);
        return Result.ok();
    }



}
