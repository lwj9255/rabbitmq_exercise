package com.hhu.consume;

import com.hhu.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserPointsListener {
    @RabbitListener(queues = {RabbitMQConfig.USER_POINTS_QUEUE})
    public void consume(String msg, Channel channel, Message message) throws Exception {
        // 预扣除优惠券
        Thread.sleep(400);
        System.out.println("用户积分预扣除成功！" + msg);
        // 手动ACK
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
