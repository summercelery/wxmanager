package springboot.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.entity.Result;

@RestController
@RequestMapping("user")
@Validated
public class UserController {


    @RequiresRoles("管理")
    @PostMapping("role")
    public Result role() {
        return Result.ok("只有管理员权限才可访问此接口");
    }


}
