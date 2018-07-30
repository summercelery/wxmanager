package springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            @NotBlank(message = "发送类型不能为空") String type
    ) throws IOException {
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

        return createPhoneCode(phone, type);

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


    /**
     * 根据发送的验证码类型 发送并创建验证码
     *
     * @param phone
     * @param type
     * @return
     */
    private Result createPhoneCode(String phone, String type) throws IOException {

        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        PhoneCode phoneCode = new PhoneCode();
        phoneCode.setPhone(phone);
        phoneCode.setType(type);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        phoneCode.setExpireTime(calendar.getTime());
        phoneCode.setCode(code);
        phoneCode.setMessage("验证码为" + code);
        if (1 == phoneCodeService.createPhoneCode(phoneCode)) {

            //发送信息存入redis
            //stringRedisTemplate.opsForValue().set(type+phone,phone,60, TimeUnit.SECONDS);
            jedisService.setWithTimeAndIndex(type + phone, "1min mark", 60, REDIS_PHONE_COUNT_DOWN_DB);

            ObjectMapper mapper = new ObjectMapper();
            try {
                phoneOverStock = jedisService.putMessage(REDIS_SEND_PHONE_DB, REDIS_1DB_KEY, mapper.writeValueAsString(phoneCode));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("json转换异常:" + phoneCode.getPhone());
            }

            //测试阶段向前端返回验证码
            return Result.ok(phoneCode.getCode());
        }

        return Result.fail("服务器异常");
    }

}
