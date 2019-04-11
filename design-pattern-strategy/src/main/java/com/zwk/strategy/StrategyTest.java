package com.zwk.strategy;

import com.zwk.strategy.promotions.PromotionStrategyFactory;

/**
 * 策略模式测试
 * @author zwk
 */
public class StrategyTest {

    public static void main(String[] args) {
        String promotionName = "discount";
        double payMoney = 10000;
        System.out.println("优惠后的应支付金额:" + PromotionStrategyFactory.getPromotionStrategy(promotionName).pay(payMoney));
    }

}
