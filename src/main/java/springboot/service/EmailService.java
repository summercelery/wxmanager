package springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.entity.Email;
import springboot.util.JsonUtil;

import static springboot.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

@Service
public class EmailService {

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 将text邮件放置入RabbitMQ
     * @param mail
     * @throws JsonProcessingException
     */
    public void sendTextMail(Email mail) throws JsonProcessingException {
        MessageProperties messageProperties = new MessageProperties();
        Message message = new Message(JsonUtil.objectToJson(mail).getBytes(),messageProperties);
        amqpTemplate.send(PHONE_CODE_QUEUE_NAME, message);

    }
}
