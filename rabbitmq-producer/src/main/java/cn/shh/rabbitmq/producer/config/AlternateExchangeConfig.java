package cn.shh.rabbitmq.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 高级特性 - 备份交换机
 */
@Configuration
public class AlternateExchangeConfig {
    public static final String EXCHANGE_NAME_OF_NBACKUP= "test_exchange_of_nbackup";
    public static final String QUEUE_NAME_OF_NBACKUP = "test_queue_of_nbackup";

    public static final String EXCHANGE_NAME_OF_BACKUP= "test_exchange_of_backup";
    public static final String QUEUE_NAME_OF_BACKUP = "test_queue_of_backup";

    @Bean
    public DirectExchange directExchangeOfNBackup(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange", EXCHANGE_NAME_OF_BACKUP);
        return new DirectExchange(EXCHANGE_NAME_OF_NBACKUP, true, false, arguments);
    }
    @Bean
    public Queue queueOfNBackup(){
        return new Queue(QUEUE_NAME_OF_NBACKUP);
    }
    @Bean
    public Binding bindingNAlternateExchange(@Qualifier("queueOfNBackup") Queue queue,
                                        @Qualifier("directExchangeOfNBackup") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("rk.nbackup");
    }

    @Bean
    public FanoutExchange fanoutExchangeOfBackup(){
        return new FanoutExchange(EXCHANGE_NAME_OF_BACKUP, true, false);
    }
    @Bean
    public Queue queueOfBackup(){
        return new Queue(QUEUE_NAME_OF_BACKUP);
    }
    @Bean
    public Binding bindingAlternateExchange(@Qualifier("queueOfBackup") Queue queue,
                                        @Qualifier("fanoutExchangeOfBackup") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}