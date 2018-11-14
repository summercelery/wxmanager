package springboot.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义登陆异常
 */
public class LoginException extends AuthenticationException {

    private String message;
    public LoginException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
