package com.tjrac.wbcost.configs.shiro.filter;

import com.tjrac.wbcost.configs.Const;
import com.tjrac.wbcost.configs.shiro.JWTToken;
import com.tjrac.wbcost.configs.shiro.JWTUtils;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.service.SysUserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class JwtAuthFilter extends AuthenticatingFilter {
	private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
	
    private static final int tokenRefreshInterval = 300;
    @Autowired
    private SysUserService userService;

    public JwtAuthFilter(SysUserService userService){
        this.userService = userService;
        this.setLoginUrl("/doLogin");
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //对于OPTION请求做拦截，不做token校验
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request, response))
        {
            return true;
        }
        Boolean afterFiltered = (Boolean)(request.getAttribute("jwtShiroFilter.FILTERED"));
        if( BooleanUtils.isTrue(afterFiltered))
        {
            return true;
        }

        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            log.error("Not found any token");
        }catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if(StringUtils.isNotBlank(jwtToken)&&!JWTUtils.isTokenExpired(jwtToken))
        {
            return new JWTToken(jwtToken);
        }
        return null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(401);
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JWTToken){
            JWTToken jwtToken = (JWTToken)token;
            SysUser user = (SysUser) subject.getPrincipal();
            boolean shouldRefresh = shouldTokenRefresh(Objects.requireNonNull(JWTUtils.getIssuedAt(jwtToken.getToken())));
            if(shouldRefresh) {
                newToken = JWTUtils.sign(user.getUsername(), Const.MD5_PWD_SALT, 3600);
            }
        }
        if(StringUtils.isNotBlank(newToken))
        {
            httpResponse.setHeader("x-auth-token", newToken);
        }
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("x-auth-token");
        return StringUtils.removeStart(header, "Bearer ");
    }

    protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }
}
