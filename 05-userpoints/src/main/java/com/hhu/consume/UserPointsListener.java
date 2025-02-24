package com.hhu.consume;

import com.hhu.config.RabbitMQConfig;
import com.hhu.service.UserPointsConsume;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPointsListener {
    @Autowired
    private UserPointsConsume userPointsConsume;

    @RabbitListener(queues = {RabbitMQConfig.USER_POINTS_QUEUE})
    public void consume(String msg, Channel channel, Message message) throws Exception {
        // 消费
        userPointsConsume.consume(message);

        // 手动ACK
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
