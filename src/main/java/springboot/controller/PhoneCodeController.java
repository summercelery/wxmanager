package springboot.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;

import static springboot.constant.RedisConstant.*;
import static springboot.constant.sessionConstant.*;

@RestController
@RequestMapping("phonecode")
@Validated
public class PhoneCodeController {

    private static Logger logger = LoggerFactory.getLogger(PhoneCodeController.class);

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhoneCodeService phoneCodeService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;


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
            //session中删除获取的图片验证码
            currentSubject.getSession().removeAttribute(VALIDATE_CODE_SESSION);
            return Result.fail("验证码错误");
        }
        //session中删除获取的图片验证码
        currentSubject.getSession().removeAttribute(VALIDATE_CODE_SESSION);

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
        if("register".equals(type)){
            currentSubject.getSession().setAttribute(REGISTER_PHONE_CODE_ID_SESSION,phoneCode.getId());
        }else if("login".equals(type)){
            currentSubject.getSession().setAttribute(LOGIN_PHONE_CODE_ID_SESSION,phoneCode.getId());

        }

        return Result.ok();

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
