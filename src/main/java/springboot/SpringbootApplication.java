package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//自动扫描包
@MapperScan("springboot.mapper")
//@EnableAsync //启用异步任务支持
@EnableScheduling //启用定时任务支持
//@EnableRedisHttpSession //启用redis作为session存储(已由shiro具体实现)
public class SpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}

