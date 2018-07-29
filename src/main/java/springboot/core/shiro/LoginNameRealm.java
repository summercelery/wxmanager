package springboot.core.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.entity.User;
import springboot.service.UserService;

/**
 * 用户名密码登陆的权限验证
 */
public class LoginNameRealm extends AuthorizingRealm {


    @Override
    public String getName(){
        return "loginNameRealm";
    }

    @Autowired
    private UserService userService;


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String loginName = token.getUsername();

        //邮箱或手机号登陆
        User user = userService.findUserByEmail(loginName);
        if(null == user){
            user = userService.findUserByPhone(loginName);
        }
        if(null == user){
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(user.getStringRoles());
        return authorizationInfo;
    }
}
