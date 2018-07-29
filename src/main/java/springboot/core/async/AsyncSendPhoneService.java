package springboot.core.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import springboot.core.redis.JedisService;
import springboot.entity.PhoneCode;
import springboot.service.PhoneCodeService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static springboot.constant.RedisConstant.REDIS_1DB_KEY;
import static springboot.constant.RedisConstant.REDIS_SEND_PHONE_DB;

/**
 * 异步调用短信发送接口线程
 */
@Service
public class AsyncSendPhoneService {

    private static Logger logger = LoggerFactory.getLogger(AsyncSendPhoneService.class);

    @Autowired
    private JedisService jedisService;
    @Autowired
    private PhoneCodeService phoneCodeService;

    @Async
    public void sendPhone(){
        PhoneCode phoneCode = null;

        while (true){
            List<String> message = jedisService.getMessage(REDIS_1DB_KEY,REDIS_SEND_PHONE_DB);
            ObjectMapper mapper = new ObjectMapper();
            try {
                phoneCode = mapper.readValue(message.get(1), PhoneCode.class);

                if(null !=phoneCode.getExpireTime() && System.currentTimeMillis() > phoneCode.getExpireTime().getTime()){
                   phoneCode.setState("expire");
                }else{
                    //TODO 调用短信发送接口

                    if(true){
                        phoneCode.setState("success");
                        phoneCode.setSendTime(new Date());
                    }else{
                        phoneCode.setState("fail");
                    }

                }

                phoneCodeService.updatePhoneCode(phoneCode);
                logger.debug(phoneCode.getPhone()+"已发送短信内容："+phoneCode.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("json转换错误");
            }
        }
    }
}
