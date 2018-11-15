package springboot.wxcms.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.PhoneCode;
import springboot.wxcms.mapper.PhoneCodeMapper;
import springboot.core.util.JsonUtil;

import java.io.IOException;
import java.util.Date;

import static springboot.core.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

/**
 * 异步调用短信发送
 */
@Service
@RabbitListener(queues = PHONE_CODE_QUEUE_NAME)
@Slf4j
public class AsyncSendPhoneService {

    @Autowired
    private PhoneCodeMapper phoneCodeMapper;

    @RabbitHandler
    public void sendPhone(String stringPhoneCode) {
        PhoneCode phoneCode = null;
        try {
            phoneCode = JsonUtil.jsonToObject(stringPhoneCode, PhoneCode.class);
            if (phoneCode.getExpireTime().after(new Date())) {
                //TODO 发送短信
                phoneCode.setSendTime(new Date());
                phoneCode.setState("success");

                log.debug(phoneCode.getPhone() + "已发送短信内容：" + phoneCode.getMessage());
            } else {
                phoneCode.setState("expire");
            }
        } catch (IOException e) {
            log.debug("短信转换错误");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if (null != phoneCode) {
                phoneCodeMapper.updateByPrimaryKeySelective(phoneCode);
            }
        }
    }
}
