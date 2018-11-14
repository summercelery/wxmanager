package springboot.core.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import springboot.wxcms.entity.SysUser;
import springboot.core.util.Md5Util;
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
        SysUser user = (SysUser)info.getPrincipals().getPrimaryPrincipal();
        return password.equals(Md5Util.getMd5(userPassword,user.getId()));
    }
}
