package springboot.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springboot.entity.Email;
import springboot.service.EmailService;
import springboot.util.UUIDUtil;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AsyncSendEmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void receive() throws JsonProcessingException {
        Email email = new Email();
        Context context = new Context();
        String uuid = UUIDUtil.getUUID();

        context.setVariable("id",uuid);
        String htmlContext = templateEngine.process("template/registerEmailTemplate",context);


        email.setReceiveAddress("465184205@qq.com");
        email.setTitle("标题");
        emailService.sendHtmlMail(email);

    }

}