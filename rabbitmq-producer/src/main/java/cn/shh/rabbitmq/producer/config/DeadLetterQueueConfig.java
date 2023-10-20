package cn.shh.rabbitmq.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 高级特性 - 死信队列
 */
@Configuration
public class DeadLetterQueueConfig {
    public static final String EXCHANGE_NAME_OF_NDLX = "test_exchange_of_ndlx";
    public static final String QUEUE_NAME_OF_NDLX = "test_queue_of_ndlx";
    public static final String EXCHANGE_NAME_OF_DLX = "test_exchange_of_dlx";
    public static final String QUEUE_NAME_OF_DLX = "test_queue_of_dlx";
    @Bean
    public TopicExchange topicExchangeOfNDlx(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME_OF_NDLX).durable(true).build();
    }
    @Bean
    public Queue queueOfNDlx(){
        return QueueBuilder.durable(QUEUE_NAME_OF_NDLX).ttl(5000)
                .deadLetterExchange(EXCHANGE_NAME_OF_DLX)
                .deadLetterRoutingKey("rk.dlx.hi")
                .maxLength(3)
                .build();
    }
    @Bean
    public Binding bindingTopicExchangeOfNDlx(@Qualifier("queueOfNDlx") Queue queue,
                                              @Qualifier("topicExchangeOfNDlx") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("rk.ndlx.#");
    }
    @Bean
    public TopicExchange topicExchangeOfDlx(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME_OF_DLX).durable(true).build();
    }
    @Bean
    public Queue queueOfDlx(){
        return QueueBuilder.durable(QUEUE_NAME_OF_DLX).build();
    }
    @Bean
    public Binding bindingTopicExchangeOfDlx(@Qualifier("queueOfDlx") Queue queue,
                                             @Qualifier("topicExchangeOfDlx") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("rk.dlx.#");
    }
}
