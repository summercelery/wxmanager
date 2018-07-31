package springboot.service.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import springboot.entity.Email;
import springboot.util.JsonUtil;

import java.io.IOException;

import static springboot.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

@Service
public class AsyncSendEmailService {

    private static Logger log = LoggerFactory.getLogger(AsyncSendEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @RabbitListener(queues = PHONE_CODE_QUEUE_NAME)
    public void receive(String stringMail) throws IOException {

        Email email = JsonUtil.jsonToObject(stringMail,Email.class);

        log.debug("发送邮件："+email.getId()+" To "+email.getReceiveAddress());


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getSendAddress());
        message.setTo(email.getReceiveAddress());
        message.setSubject(email.getTitle());
        message.setText(email.getContent());

        javaMailSender.send(message);

    }
}
