package springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springboot.entity.Email;
import springboot.entity.Result;
import springboot.service.EmailService;
import springboot.util.UUIDUtil;

@RestController
@RequestMapping("email")
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
