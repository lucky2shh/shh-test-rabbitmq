package cn.shh.rabbitmq.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProducerCconfig {
    /**
     * 交换机
     */
    public static final String EXCHANGE_NAME_OF_FANOUT = "test_exchange_of_fanout";
    public static final String EXCHANGE_NAME_OF_DIRECT = "test_exchange_of_direct";
    public static final String EXCHANGE_NAME_OF_TOPIC = "test_exchange_of_topic";
    public static final String EXCHANGE_NAME_OF_HEADERS = "test_exchange_of_headers";

    @Bean
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder.fanoutExchange(EXCHANGE_NAME_OF_FANOUT).durable(true).build();
    }
    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(EXCHANGE_NAME_OF_DIRECT).durable(true).build();
    }
    @Bean
    public TopicExchange topicExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME_OF_TOPIC).durable(true).build();
    }
    @Bean
    public HeadersExchange headersExchange(){
        return ExchangeBuilder.headersExchange(EXCHANGE_NAME_OF_HEADERS).durable(true).build();
    }

    /**
     * 队列
     */
    public static final String QUEUE_NAME_OF_FANOUT = "test_queue_of_fanout";
    public static final String QUEUE_NAME_OF_DIRECT = "test_queue_of_direct";
    public static final String QUEUE_NAME_OF_TOPIC = "test_queue_of_topic";
    public static final String QUEUE_NAME_OF_HEADERS = "test_queue_of_headers";
    @Bean
    public Queue queueOfFanout(){
        return QueueBuilder.durable(QUEUE_NAME_OF_FANOUT).build();
    }
    @Bean
    public Queue queueOfDirect(){
        return QueueBuilder.durable(QUEUE_NAME_OF_DIRECT).build();
    }
    @Bean
    public Queue queueOfTopic(){
        return QueueBuilder.durable(QUEUE_NAME_OF_TOPIC).build();
    }
    @Bean
    public Queue queueOfHeaers(){
        return QueueBuilder.durable(QUEUE_NAME_OF_HEADERS).build();
    }

    /**
     * 绑定交换机和队列
     */
    @Bean
    public Binding bindingFanoutExchange(@Qualifier("queueOfFanout") Queue queue,
                                         @Qualifier("fanoutExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }
    @Bean
    public Binding bindingDirectExchange(@Qualifier("queueOfDirect") Queue queue,
                                         @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("rk");
    }
    @Bean
    public Binding bindingTopicExchange(@Qualifier("queueOfTopic") Queue queue,
                                         @Qualifier("topicExchange") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("rk.base.#");
    }
    /*@Bean
    public Binding bindingHeadersExchange(@Qualifier("queueOfHeaders") Queue queue,
                                         @Qualifier("headersExchange") HeadersExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).where("key").exists();
    }*/


}
