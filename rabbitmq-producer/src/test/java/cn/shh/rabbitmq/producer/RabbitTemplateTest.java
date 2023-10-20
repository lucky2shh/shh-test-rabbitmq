package cn.shh.rabbitmq.producer;

import cn.shh.rabbitmq.producer.config.AlternateExchangeConfig;
import cn.shh.rabbitmq.producer.config.DeadLetterQueueConfig;
import cn.shh.rabbitmq.producer.config.DelayExchangeConfig;
import cn.shh.rabbitmq.producer.config.RabbitMQProducerCconfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RabbitTemplateTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 基础功能测试
     */
    @Test
    public void testSendToFanoutExchange() {
        rabbitTemplate.convertAndSend(RabbitMQProducerCconfig.EXCHANGE_NAME_OF_FANOUT,
                "", "hello fanout exchange.");
    }

    @Test
    public void testSendToDirectExchange() {
        rabbitTemplate.convertAndSend(RabbitMQProducerCconfig.EXCHANGE_NAME_OF_DIRECT,
                "rk", "hello direct exchange.");
    }

    @Test
    public void testSendToTopicExchange() {
        rabbitTemplate.convertAndSend(RabbitMQProducerCconfig.EXCHANGE_NAME_OF_TOPIC,
                "rk.hi", "hello topic exchange.");
    }
    /*@Test
    public void testSendToHeadersExchange(){
        rabbitTemplate.convertAndSend(RabbitMQProducerCconfig.EXCHANGE_NAME_OF_HEADERS,
                "", "hello fanout exchange.");
    }*/


    /**
     * 高级特性测试 - confirm and return
     * <p>
     * 无论消息是否到达交换机都会执行 confirm callback。
     * 消息转发至队列失败时会执行 return callback。
     */
    @Test
    public void testConfirmCallback() {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()) {
                System.out.println("消息成功到达交换机，消息id: " + correlationData.getId());
            } else {
                System.out.println("消息到达交换机失败，消息id: " + correlationData.getId());
            }
        }, ex -> {
            System.out.println("消息发送失败，异常信息：" + ex);
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("return callback 执行了，消息：" + returnedMessage);
            }
        });

        rabbitTemplate.convertAndSend(RabbitMQProducerCconfig.EXCHANGE_NAME_OF_DIRECT, "rk",
                "this is a msg of test confirm callback.", correlationData);
    }

    /**
     * 高级特性测试 - 死信队列
     */
    @Test
    public void testDlx() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend(DeadLetterQueueConfig.EXCHANGE_NAME_OF_NDLX, "rk.ndlx.hi",
                    i + " this is a msg of test dlx.");
        }
    }

    /**
     * 高级特性测试 - 延迟队列（插件模式）
     *
     * 虽然死信队列+TTL可以实现延迟队列的功能和效果，但存在超时时间优先级错误问题，
     * 要想解决这个问题，那么推荐使用插件方式的延迟队列功能。（也就是本例子）
     */
    @Test
    public void testDelay() {
        rabbitTemplate.convertAndSend(DelayExchangeConfig.EXCHANGE_NAME_OF_DELAY, "rk.delay",
                "(20s) this is a msg of test delay.", message -> {
                    message.getMessageProperties().setDelay(20000);
                    return message;
                });
        rabbitTemplate.convertAndSend(DelayExchangeConfig.EXCHANGE_NAME_OF_DELAY, "rk.delay",
                "(5s) this is a msg of test delay.", message -> {
                    message.getMessageProperties().setDelay(5000);
                    return message;
                });
    }

    /**
     * 高级特性测试 - 备份交换机
     */
    @Test
    public void testBackup() {
        rabbitTemplate.convertAndSend(AlternateExchangeConfig.EXCHANGE_NAME_OF_NBACKUP, "rk.nbackup",
                "this is a msg of test nbackup.");
        rabbitTemplate.convertAndSend(AlternateExchangeConfig.EXCHANGE_NAME_OF_NBACKUP, "rk.nbackup123",
                "this is a msg of test backup.");
    }
}