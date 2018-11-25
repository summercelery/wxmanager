package springboot.core.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springboot.core.core.redis.JedisService;

import static springboot.core.constant.RabbitMQConstant.PHONE_CODE_QUEUE_NAME;
import static springboot.core.constant.RedisConstant.*;

@Component
public class ScheduleTask {

//    @Autowired
//    private JedisService jedisService;
//
//    @Autowired
//    private AmqpTemplate amqpTemplate;
//
//    @Scheduled(fixedDelay = 1000)
//    public void clockSendPhone() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        for(String key : jedisService.getAllkeys(REDIS_CLOCK_PHONE_DB)){
//            if(System.currentTimeMillis() > Long.valueOf(jedisService.getHashField(key,"clockTime",REDIS_CLOCK_PHONE_DB))){
//                String data = objectMapper.writeValueAsString(jedisService.getAllHash(key,REDIS_CLOCK_PHONE_DB));
//                amqpTemplate.convertAndSend(PHONE_CODE_QUEUE_NAME,data);
//                jedisService.deleteKey(key,REDIS_CLOCK_PHONE_DB);
//            }
//        }
//    }
}
