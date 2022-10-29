package top.meyok.user.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.util.ObjectUtils;
import top.meyok.user.pojo.dto.EmailPasswordSaltDTO;
import top.meyok.user.service.UserService;
import top.meyok.user.shiro.other.SerializeSimpleByteSource;
import top.meyok.user.util.ApplicationContextUtils;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/8/31 12:38
 */
public class CustomerRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("user");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String email = (String) authenticationToken.getPrincipal();
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        EmailPasswordSaltDTO passwordInfo = userService.getPasswordInfo(email);
        if (!ObjectUtils.isEmpty(passwordInfo)) {
            return new SimpleAuthenticationInfo(passwordInfo.getEmail(), passwordInfo.getPassword(), new SerializeSimpleByteSource(passwordInfo.getSalt()), this.getName());
//            return new SimpleAuthenticationInfo(passwordInfo.getEmail(), passwordInfo.getPassword(), ByteSource.Util.bytes(passwordInfo.getSalt()), this.getName());
        }

        return null;
    }
}
