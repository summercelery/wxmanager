package springboot.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import springboot.entity.User;
import springboot.util.Md5Util;

import java.security.NoSuchAlgorithmException;

public class CredentialsMather extends SimpleCredentialsMatcher  {

    /**
     * 自定义用户名密码登陆方式的加密后的密码的比对
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String userPassword = new String (usernamePasswordToken.getPassword());
        String password = (String)info.getCredentials();
        User user = (User)info.getPrincipals().getPrimaryPrincipal();
        try {
            return password.equals(Md5Util.getMd5(userPassword,user.getId()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
