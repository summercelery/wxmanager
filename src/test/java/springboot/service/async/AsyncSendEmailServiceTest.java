package springboot.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.entity.Email;
import springboot.service.EmailService;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AsyncSendEmailServiceTest {



    @Autowired
    private EmailService emailService;

    @Test
    public void receive() throws JsonProcessingException {
        Email email = new Email();
        email.setContent("内容");
        email.setReceiveAddress("123456");
        email.setTitle("标题");
        emailService.sendTextMail(email);

    }

}