package springboot.core.shiro;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.entity.PhoneCode;
import springboot.entity.User;
import springboot.exception.LoginException;
import springboot.service.PhoneCodeService;
import springboot.service.UserService;

/**
 * 手机号登陆的权限验证
 */
public class PhoneRealm extends AuthorizingRealm{


    @Autowired
    private UserService userService;
    @Autowired
    private PhoneCodeService phoneCodeService;

    @Override
    public String getName() {
        return "phoneRealm";
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;

        User user = userService.findUserByPhone(token.getUsername());

        if(null == user){
            throw new UnknownAccountException();
        }
        //TODO 放入redis
        PhoneCode phone = null;
//        phoneCodeService.findPhoneCodeByPhoneAndCodeAndType(user.getPhone(),String.valueOf(token.getPassword()),"login");
        if(null == phone){
            throw new IncorrectCredentialsException();
        }else if(System.currentTimeMillis() > phone.getExpireTime().getTime()){
            throw new LoginException("验证码已过期");
        }

        return new SimpleAuthenticationInfo(user,phone.getCode(),getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(user.getStringRoles());
        return authorizationInfo;
    }


}
