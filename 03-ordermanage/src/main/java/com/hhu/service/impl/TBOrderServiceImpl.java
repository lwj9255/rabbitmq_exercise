package com.hhu.service.impl;

import com.hhu.config.RabbitMQConfig;
import com.hhu.mapper.TBOrderMapper;
import com.hhu.service.TBOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class TBOrderServiceImpl implements TBOrderService {
    @Resource
    private TBOrderMapper tbOrderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void save() {
        // 生成订单标识
        String id = UUID.randomUUID().toString();
        // 创建订单
        tbOrderMapper.save(id);
        // 发送消息到死信队列
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE,"",id, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("15000");
                return message;
            }
        });

    }

    @Override
    public void delayCancelOrder(String id) {
        //1. 基于id查询订单状态，防止在取消订单过程中，订单被支付，用for update锁住该行数据
        int orderState = tbOrderMapper.findOrderStateByIdForUpdate(id);
        //2. 判断订单状态,如果已支付，则直接返回
        if(orderState != 0){
            log.info("订单已支付！");
            return;
        }
        //3. 如果没支付，那么取消订单
        log.info("订单未支付");
        tbOrderMapper.updateOrderState(-1,id);
    }
}
