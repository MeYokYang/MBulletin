package top.meyok.user.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import top.meyok.user.constants.ShiroConstants;
import top.meyok.user.shiro.cache.RedisCacheManager;
import top.meyok.user.shiro.dao.CustomSessionDAO;
import top.meyok.user.shiro.realm.CustomerRealm;

import java.util.HashMap;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/8/31 8:17
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置ShiroFilter，拦截请求
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        shiroFilterFactoryBean.setLoginUrl("http://localhost:3000/login");

        HashMap<String, String> map = new HashMap<>();
        // 页面相关
//        map.put("/static/home.html", "roles[user]");
//        map.put("/static/**", "anon");

        // 接口相关
        map.put("/users/**", "anon");

        map.put("/**", "authc");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }


    /**
     * 设置SecurityManager，向其中设置自定义realm
     * @param realm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm, DefaultWebSessionManager defaultWebSessionManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 设置realm
        defaultWebSecurityManager.setRealm(realm);

        // 设置redis缓存
        defaultWebSecurityManager.setCacheManager(new RedisCacheManager());

        // 设置session管理器
        defaultWebSecurityManager.setSessionManager(defaultWebSessionManager);

        return defaultWebSecurityManager;
    }



    /**
     * 设置自定义realm，包含凭证校验匹配器、缓存。
     * @return
     */
    @Bean
    public Realm getRealm() {
        CustomerRealm customerRealm = new CustomerRealm();

        // 修改凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ShiroConstants.ENCRYPTION_ALGORITHM);
        hashedCredentialsMatcher.setHashIterations(ShiroConstants.HASH_ITERATION);
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        // 开启全局缓存
        customerRealm.setCachingEnabled(true);
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("authentication-mbulletin");
        customerRealm.setAuthorizationCacheName("authorization-mbulletin");


        return customerRealm;
    }


    /**
     * 自定义session管理器
     * @param customSessionDAO
     * @return
     */
    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager(CustomSessionDAO customSessionDAO) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();

        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setGlobalSessionTimeout(1800000);
        defaultWebSessionManager.setSessionDAO(customSessionDAO);

        return defaultWebSessionManager;
    }

}
