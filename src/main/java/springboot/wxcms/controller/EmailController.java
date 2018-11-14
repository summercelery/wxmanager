package springboot.wxcms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.wxcms.entity.Result;
import springboot.wxcms.service.EmailService;

@RestController
@RequestMapping("email")
@Validated
public class EmailController {

    @Autowired
    private EmailService emailService;



    @Value("${spring.mail.username}")
    private String sendAddress;

    @GetMapping("send")
    public Result sengMail() throws JsonProcessingException {
//        Email email = new Email();
//
//        Context context = new Context();
//        String id = UUIDUtil.getUUID();
//        context.setVariable("id", id);
////        templateEngine.process("template/registerEmailTemplate.html",context);
//
//        email.setContent("内容");
//        email.setSendAddress(sendAddress);
//        email.setReceiveAddress("465184205@qq.com");
//        email.setTitle("标题");
//        emailService.createEmail(email);
//        emailService.sendMail(email);

        return Result.ok();
    }


}
