package springboot.wxcms.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("login")
    public String login(){
        return "login";
    }


}
