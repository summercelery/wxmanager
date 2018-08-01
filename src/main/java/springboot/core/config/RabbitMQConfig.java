package springboot.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static springboot.constant.RabbitMQConstant.EMAIL_QUEUE_NAME;
import static springboot.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue phoneQueue(){
        //创建队列并持久化
        return new Queue(PHONE_CODE_QUEUE_NAME,true);
    }

    @Bean
    public Queue emailQueue(){
        return new Queue(EMAIL_QUEUE_NAME,true);
    }
}
