package com.hhu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceOrderController {

    /**
     * 模拟用户下单操作
     * @return
     */
    @GetMapping("/po")
    public String po(){
        //1、调用库存服务扣除商品库存

        //2、调用订单服务，创建订单

        //3、调用优惠券服务，预扣除使用的优惠券

        //4、调用用户积分服务，预扣除用户使用的积分

        //5、调用商家服务，通知商家用户已下单

        return "place order is ok!";
    }

}
