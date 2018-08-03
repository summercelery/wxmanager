package springboot.core.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.entity.PhoneCode;
import springboot.entity.User;
import springboot.exception.LoginException;
import springboot.service.PhoneCodeService;
import springboot.service.UserService;

import java.util.Date;

import static springboot.constant.sessionConstant.LOGIN_PHONE_CODE_ID_SESSION;

/**
 * 手机号登陆的权限验证
 */
public class PhoneRealm extends AuthorizingRealm {

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

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findUserByPhone(token.getUsername());
        if (null == user) {
            throw new UnknownAccountException();
        }
        PhoneCode phoneCode = null;
        Session session = SecurityUtils.getSubject().getSession();
        if (null != session.getAttribute(LOGIN_PHONE_CODE_ID_SESSION) && StringUtils.isNotBlank(session.getAttribute(LOGIN_PHONE_CODE_ID_SESSION).toString())) {
            phoneCode = phoneCodeService.findPhoneCodeById(session.getAttribute(LOGIN_PHONE_CODE_ID_SESSION).toString());
            SecurityUtils.getSubject().getSession().removeAttribute(LOGIN_PHONE_CODE_ID_SESSION);
        }
        if (null == phoneCode) {
            throw new IncorrectCredentialsException();
        } else if (phoneCode.getExpireTime().before(new Date())) {
            throw new LoginException("验证码已过期");
        }
        return new SimpleAuthenticationInfo(user, phoneCode.getCode(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(user.getStringRoles());
        return authorizationInfo;
    }


}
