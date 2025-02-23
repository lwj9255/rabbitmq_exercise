package com.hhu.controller;

import com.hhu.client.*;
import com.hhu.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceOrderController {
    @Autowired
    private ItemStockClient itemStockClient;
    @Autowired
    private OrderManageClient orderManageClient;
    @Autowired
    private CouponClient couponClient;
    @Autowired
    private UserPointsClient userPointsClient;
    @Autowired
    private BusinessClient businessClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 模拟用户下单操作
     * @return
     */
    @GetMapping("/po")
    public String po(){
        long start = System.currentTimeMillis();
        //1、调用库存服务扣除商品库存
        itemStockClient.decr();
        //2、调用订单服务，创建订单
        orderManageClient.create();
        /*//3、调用优惠券服务，预扣除使用的优惠券
        couponClient.coupon();
        //4、调用用户积分服务，预扣除用户使用的积分
        userPointsClient.up();
        //5、调用商家服务，通知商家用户已下单
        businessClient.notifyBusiness();*/

        //3. 将同步方式修改为基于RabbitMQ的异步方式
        String userAndOrderInfo = "用户信息和订单信息和优惠券信息等";
        rabbitTemplate.convertAndSend(RabbitMQConfig.PLACE_ORDER_EXCHANGE,"",userAndOrderInfo);

        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
        return "place order is ok!";
    }

}
