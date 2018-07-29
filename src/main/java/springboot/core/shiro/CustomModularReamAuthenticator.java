package springboot.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import springboot.exception.LoginException;

import java.util.Collection;

/**
 * 登陆token与所需的realm进行一一对应
 */
public class CustomModularReamAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {

        this.assertRealmsConfigured();
        Collection<Realm> realms = this.getRealms();
        CustomerAuthenticationToken token = (CustomerAuthenticationToken) authenticationToken;
        Realm loginRealm = null;
        if("phone".equals(token.getLoginType())){
           loginRealm = getOneRealm("phoneRealm");
        }else if("loginName".equals(token.getLoginType())){
           loginRealm = getOneRealm("loginNameRealm");
        }else{
            throw new LoginException("登录类型错误");
        }

        return doSingleRealmAuthentication(loginRealm,authenticationToken);
    }
    private Realm getOneRealm(String realmName){
        Collection<Realm> realms = this.getRealms();
        for (Realm realm : realms){
            if(realmName.equals(realm.getName())){
                return realm;
            }
        }
        return null;
    }


}
