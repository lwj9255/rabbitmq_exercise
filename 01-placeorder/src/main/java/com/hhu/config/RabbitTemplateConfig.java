package com.hhu.config;


import com.hhu.mapper.ResendMapper;
import com.hhu.util.GlobalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@Slf4j
public class RabbitTemplateConfig {
    @Resource
    ResendMapper resendMapper;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        //1、new出RabbitTemplate对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        //2、将connectionFactory设置到RabbitTemplate对象中
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //3、设置confirm回调
        rabbitTemplate.setConfirmCallback(confirmCallback());
        //4、设置return回调
        rabbitTemplate.setReturnCallback(returnCallback());
        //5、设置mandatory为true
        rabbitTemplate.setMandatory(true);
        //6、返回RabbitTemplate对象即可
        return rabbitTemplate;
    }

    public RabbitTemplate.ConfirmCallback confirmCallback(){
        return new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (correlationData == null) return;
                String msgId = correlationData.getId();
                if(ack){
                    log.info("消息发送到Exchange成功!! msgId = " + msgId);
                    GlobalCache.remove(msgId);
                }else{
                    log.error("消息发送到Exchange失败!! msgId = " + msgId);
                    Map map = (Map) GlobalCache.get(msgId);
                    resendMapper.save(map);
                }
            }
        };
    }

    public RabbitTemplate.ReturnCallback returnCallback(){
        return new RabbitTemplate.ReturnCallback(){
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.error("消息未路由到队列");
                log.error("return：消息为：" + new String(message.getBody()));
                log.error("return：交换机为：" + exchange);
                log.error("return：路由为：" + routingKey);
            }
        };
    }

}
