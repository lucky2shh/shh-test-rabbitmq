package cn.shh.rabbitmq.consumer.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListener {

    /*@RabbitListener(queues = {"test_queue_of_fanout", "test_queue_of_direct",
            "test_queue_of_topic", "test_queue_of_headers"})
    public void listenerQueue(Message message){
        System.out.println("message = " + message);
    }*/

    /**
     * 高级特性测试 - 消费端消息确认
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {"test_queue_of_direct"})
    public void listenerQueueOfDirect(Message message, Channel channel)  {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println("message = " + message);
            // 第一个参数表示当前消息的标记，第二参数true表示签收所有未未签收的消息，false表示仅签收指定标记的消息。
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                // 第二个参数表示是否将全部消息退回，第三个参数表示是否重新发送消息。
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 高级特性测试 - 死信队列
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {"test_queue_of_dlx"})
    public void listenerQueueOfDlx(Message message, Channel channel)  {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println("message = " + message);
            // 第一个参数表示当前消息的标记。
            // 第二参数true表示签收所有未签收的消息，false表示仅签收指定标记的消息。
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                // 第二个参数表示是否将全部消息退回，第三个参数表示是否重新发送消息。
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 高级特性 - 延迟队列
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {"test_queue_of_delay"})
    public void listenerQueueOfDelay(Message message, Channel channel)  {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println("message = " + message);
            // 第一个参数表示当前消息的标记。
            // 第二参数true表示签收所有未未签收的消息，false表示仅签收指定标记的消息。
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                // 第二个参数表示是否将全部消息退回，第三个参数表示是否重新发送消息。
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 高级特性 - 备份交换机
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {"test_queue_of_nbackup", "test_queue_of_backup"})
    public void listenerQueueOfBackup(Message message, Channel channel)  {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println("message = " + message);
            // 第一个参数表示当前消息的标记。
            // 第二参数true表示签收所有未未签收的消息，false表示仅签收指定标记的消息。
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                // 第二个参数表示是否将全部消息退回，第三个参数表示是否重新发送消息。
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}