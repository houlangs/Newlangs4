package com.guaning.newlangs.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类 
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器] 
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
        
                // 指定 拦截路由 与 放行路由
                .addInclude("/**")
                /* 排除掉 /favicon.ico */
                //.addExclude("/favicon.ico")
                
                // 认证函数: 每次请求执行 
                .setAuth(obj -> {
                    System.out.println("---------- 进入Sa-Token全局认证 -----------");
                    
					//登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**")
                            .notMatch("/user/email_code", "/user/login", "/user/register", "/config/scan", "/point_record/createCode")
                            .check(r -> StpUtil.checkLogin());
        
                    // 角色校验 -- 拦截路由，必须具备 管理员 角色才可以通过认证
                    SaRouter.match("/user/lock",
                            "/user/list",
                            "/role/**",
                            "/domain_config/**",
                            "/config/**",
                            "/domain/add",
                            "/domain/update",
                            "/domain/delete/**",
                            "/domain/list_from_dns")
		                    .notMatch("/config/scan")
                            .check(r -> StpUtil.checkRole("1"));
                            
                })
                
                // 异常处理函数：每次认证函数发生异常时执行此函数 
                .setError(e -> {
                    System.out.println("---------- 进入Sa-Token异常处理 -----------");
					// 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    return JSONUtil.toJsonStr(SaResult.error(e.getMessage()));
                })
                
                // 前置函数：在每次认证函数之前执行（BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入）
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                    // 服务器名称 
                    .setServer("NewLangs")
                    // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以 
                    .setHeader("X-Frame-Options", "SAMEORIGIN")
                    // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                    .setHeader("X-XSS-Protection", "1; mode=block")
                    // 禁用浏览器内容嗅探 
                    .setHeader("X-Content-Type-Options", "nosniff")
                    // ---------- 设置跨域响应头 ----------
        			// 允许指定域访问跨域资源
        			.setHeader("Access-Control-Allow-Origin", "*")
        			// 允许所有请求方式
        			.setHeader("Access-Control-Allow-Methods", "*")
        			// 允许的header参数
        			.setHeader("Access-Control-Allow-Headers", "*")
        			// 有效时间
        			.setHeader("Access-Control-Max-Age", "3600");
                
                    // 如果是预检请求，则立即返回到前端
        			SaRouter.match(SaHttpMethod.OPTIONS)
        				.free(e -> System.out.println("--------OPTIONS预检请求，不做处理"))
        				.back();
                });
    }
    
}
