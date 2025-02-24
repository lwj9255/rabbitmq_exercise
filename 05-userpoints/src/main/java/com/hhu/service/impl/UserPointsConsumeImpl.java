package com.hhu.service.impl;

import com.hhu.mapper.UserPointsIdempotentMapper;
import com.hhu.service.UserPointsConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserPointsConsumeImpl implements UserPointsConsume {
    @Resource
    private UserPointsIdempotentMapper userPointsIdempotentMapper;

    private final String ID_Name = "spring_returned_message_correlation";

    @Override
    @Transactional
    public void consume(Message message) {

        //1. 查询幂等表是否存在当前消息标识
        // 获取生产者提供的CorrelationId要基于header去获取。
        String id = message.getMessageProperties().getHeader(ID_Name);
        int count = userPointsIdempotentMapper.findById(id);
        //2. 如果存在，直接return
        if(count == 1){
            log.info("消息已经被消费！无需重复消费！");
            return;
        }
        //3. 如果不存在，插入消息标识到幂等表并消费
        userPointsIdempotentMapper.save(id);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("扣除用户积分成功！！");
    }
}
