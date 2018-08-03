package springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.entity.PhoneCode;
import springboot.mapper.PhoneCodeMapper;
import springboot.util.JsonUtil;

import java.util.Calendar;

import static springboot.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

@Service
public class PhoneCodeService {

    @Autowired
    private PhoneCodeMapper phoneCodeMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 创建并将短信放置入rabbitmq中
     * @param phone
     * @param type
     */
    public PhoneCode createAndSendPhoneCode(String phone,String type) throws JsonProcessingException {
        //验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        PhoneCode phoneCode = new PhoneCode();
        phoneCode.setPhone(phone);
        phoneCode.setType(type);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        phoneCode.setExpireTime(calendar.getTime());
        phoneCode.setCode(code);
        phoneCode.setMessage("验证码为" + code);
        phoneCodeMapper.insertSelective(phoneCode);

        amqpTemplate.convertAndSend(PHONE_CODE_QUEUE_NAME, JsonUtil.objectToJson(phoneCode));
        return phoneCode;

    }



    public int createPhoneCode(PhoneCode phoneCode){
        return phoneCodeMapper.insertSelective(phoneCode);
    }

    public PhoneCode findPhoneCodeById(String id){
        return  phoneCodeMapper.selectByPrimaryKey(id);
    }

    public int updatePhoneCode(PhoneCode phoneCode){
        return phoneCodeMapper.updateByPrimaryKeySelective(phoneCode);
    }

}
