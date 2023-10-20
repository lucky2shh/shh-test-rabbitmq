package cn.shh.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 高级特性 - 延迟交换机
 */
@Configuration
public class DelayExchangeConfig {
    public static final String EXCHANGE_NAME_OF_DELAY= "test_exchange_of_delay";
    public static final String QUEUE_NAME_OF_DELAY = "test_queue_of_delay";

    @Bean
    public CustomExchange exchangeOfDelay(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(EXCHANGE_NAME_OF_DELAY, "x-delayed-message",
                true, false, arguments);
    }
    @Bean
    public Queue queueOfDelay(){
        return new Queue(QUEUE_NAME_OF_DELAY);
    }
    @Bean
    public Binding bindingDelayExchange(@Qualifier("queueOfDelay") Queue queue,
                                        @Qualifier("exchangeOfDelay") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with("rk.delay").noargs();
    }
}
