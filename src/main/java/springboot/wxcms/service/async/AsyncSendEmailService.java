package springboot.wxcms.service.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import springboot.wxcms.entity.Email;
import springboot.wxcms.mapper.EmailMapper;
import springboot.core.util.JsonUtil;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import static springboot.core.constant.RabbitMQConstant.EMAIL_QUEUE_NAME;

@Service
@RabbitListener(queues = EMAIL_QUEUE_NAME)
public class AsyncSendEmailService {

    private static Logger log = LoggerFactory.getLogger(AsyncSendEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailMapper emailMapper;

    /**
     * 接收rabbitmq中的邮件信息并发送
     *
     * @param stringMail
     * @throws IOException
     */
    @RabbitHandler
    public void receive(String stringMail) throws IOException {
        Email email = null;

        try {
            email = JsonUtil.jsonToObject(stringMail, Email.class);
            log.debug("发送邮件：" + email.getId() + " To " + email.getReceiveAddress());
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getSendAddress());
            helper.setTo(email.getReceiveAddress());
            helper.setSubject(email.getTitle());
            helper.setText(email.getContent(),true);
            email.setSendDate(new Date());

            javaMailSender.send(message);
            email.setState("success");

        } catch (MailException e) {
            if (null != email) {
                email.setState("fail");
                log.warn("邮件发送异常" + email.getReceiveAddress());
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (null != email) {
                email.setState("error");
            }
            e.printStackTrace();
        } finally {
            if (null != email) {
                emailMapper.updateByPrimaryKeySelective(email);
            }
        }
    }
}
