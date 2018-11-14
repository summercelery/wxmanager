package springboot.wxcms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.Email;
import springboot.wxcms.mapper.EmailMapper;
import springboot.core.util.JsonUtil;

import static springboot.core.constant.RabbitMQConstant.EMAIL_QUEUE_NAME;

@Service
public class EmailService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private EmailMapper emailMapper;



    /**
     * 将Html邮件放置入RabbitMQ
     *
     * @param mail
     * @throws JsonProcessingException
     */
    public void sendHtmlMail(Email mail) throws JsonProcessingException {
        //转换一个Java对象为Amqp消息，然后再用缺省的交换机指定路由键发送消息
        amqpTemplate.convertAndSend(EMAIL_QUEUE_NAME, JsonUtil.objectToJson(mail));
    }


    /**
     * 创建email
     * @param email
     * @return
     */
    public Integer createEmail(Email email){
        return emailMapper.insertSelective(email);
    }

    public Integer countNumByEmail(String email){
        return emailMapper.countNumByEmail(email);
    }
}
