//package springboot.core.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import springboot.interceptor.LogInterceptor;
//
///**
// * 新增自定义拦截器
// *
// * springboot2.0 如有需要重写addResourceHandlers来实现静态资源的映射,不要使用application.properties中添加配置来实现映射
// */
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
//        super.addInterceptors(registry);
//    }
//}
