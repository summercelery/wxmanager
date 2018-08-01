package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springboot.service.async.AsyncSendEmailService;
import springboot.service.async.AsyncSendPhoneService;

@SpringBootApplication
//自动扫描包
@MapperScan("springboot.mapper")
@EnableAsync //启用异步任务支持
@EnableScheduling //启用定时任务支持
//@EnableRedisHttpSession //启用redis作为session存储
public class SpringbootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);

		AsyncSendPhoneService asyncTaskService =context.getBean(AsyncSendPhoneService.class);
		asyncTaskService.sendPhone();

	}
}
