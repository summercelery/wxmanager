package springboot.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static springboot.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue queue(){
        return new Queue(PHONE_CODE_QUEUE_NAME);
    }
}
