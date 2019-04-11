package com.zwk.strategy.promotions;

import org.springframework.util.Assert;

/**
 * 没有优惠
 * @author zwk
 */
public class NonePromotionStrategy implements IPromotionStrategy {

    @Override
    public double pay(double money) {
        Assert.isTrue(money > 0, "非法金额");
        return money;
    }

}
