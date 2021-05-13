package com.tjrac.wbcost.configs.shiro;

import com.tjrac.wbcost.configs.shiro.filter.AnyRolesAuthorizationFilter;
import com.tjrac.wbcost.configs.shiro.filter.JwtAuthFilter;
import com.tjrac.wbcost.configs.shiro.realm.DbShiroRealm;
import com.tjrac.wbcost.configs.shiro.realm.JWTShiroRealm;
import com.tjrac.wbcost.service.SysUserService;
import com.tjrac.wbcost.service.serviceImpl.SysUserServiceImpl;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;


/**
 * @author myd
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager, SysUserService userService) throws Exception {
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<Filter>();
        filterRegistration.setFilter((Filter) shiroFilter(securityManager, userService).getObject());
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);

        return filterRegistration;
    }

    @Bean
    public Authenticator authenticator(SysUserService userService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(jwtShiroRealm(userService), dbShiroRealm(userService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean("dbRealm")
    public Realm dbShiroRealm(SysUserService userService) {
        return new DbShiroRealm(userService);
    }

    @Bean("jwtRealm")
    public Realm jwtShiroRealm(SysUserService userService) {
        return new JWTShiroRealm(userService);
    }

    /**
     * Shiro 安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        SysUserService sysuserService = new SysUserServiceImpl();
        //设置realm
        HashSet<Realm> realms = new HashSet();
        realms.add(dbShiroRealm(sysuserService));
        realms.add(jwtShiroRealm(sysuserService));
        securityManager.setRealms(realms);
        return securityManager;
    }


    /**
     * 设置过滤器
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, SysUserService userService) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", createAuthFilter(userService));
        filterMap.put("anyRole", createRolesFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return factoryBean;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/doLogin", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/doRegister", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/static", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");

        chainDefinition.addPathDefinition("/api/public/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/api/user/**", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/api/test/**", "noSessionCreation,authcToken");
        chainDefinition.addPathDefinition("/api/admin/**", "noSessionCreation,authcToken,anyRole[0]");
        chainDefinition.addPathDefinition("/api/users/**", "noSessionCreation,authcToken,anyRole[1]");
        chainDefinition.addPathDefinition("/api/jobHunter/**", "noSessionCreation,authcToken,anyRole[2]");
        return chainDefinition;
    }

    protected JwtAuthFilter createAuthFilter(SysUserService userService) {
        return new JwtAuthFilter(userService);
    }

    protected AnyRolesAuthorizationFilter createRolesFilter() {
        return new AnyRolesAuthorizationFilter();
    }

}
