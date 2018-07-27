package springboot.core.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义登陆所需token，增加登陆类型信息
 */
public class CustomerAuthenticationToken extends UsernamePasswordToken{


    //登陆类型 phone:手机验证码登陆  loginName:用户名登陆
    private String loginType;

    public CustomerAuthenticationToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
