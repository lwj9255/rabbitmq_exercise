package com.hhu.consume;

import com.hhu.config.RabbitMQConfig;
import com.hhu.mapper.TBOrderMapper;
import com.hhu.service.TBOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DealyMessageListener {
    @Autowired
    private TBOrderService tbOrderService;

    @RabbitListener(queues = RabbitMQConfig.DEAD_QUEUE)
    public void consume(String id, Channel channel, Message message) throws IOException {
        //1. 调用service实现订单状态的处理
        tbOrderService.delayCancelOrder(id);

        //2. ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
