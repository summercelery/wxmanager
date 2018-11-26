package springboot.core.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.core.interceptor.LogInterceptor;
import springboot.core.interceptor.WxOAuth2Interceptor;

@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //添加oauth拦截器
        WxOAuth2Interceptor wxOAuth2Interceptor = new WxOAuth2Interceptor();
        wxOAuth2Interceptor.setIncludes(new String[]{"/wxweb/sendmsg.html", "/wxapi/oauthOpenid.html"});
        LogInterceptor LoginInterceptor = new LogInterceptor();

        registry.addInterceptor(LoginInterceptor).addPathPatterns("/**");
        registry.addInterceptor(wxOAuth2Interceptor).addPathPatterns("/**/*.html");
    }

//    //配置首页启动
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/views/login.html");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
}