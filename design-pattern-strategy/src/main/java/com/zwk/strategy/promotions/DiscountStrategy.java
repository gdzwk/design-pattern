package com.zwk.strategy.promotions;

import org.springframework.util.Assert;

/**
 * 打折
 * @author zwk
 */
public class DiscountStrategy implements IPromotionStrategy {

    private static final double DISCOUNT = 0.95;

    @Override
    public double pay(double money) {
        Assert.isTrue(money > 0, "非法金额");
        return money * DISCOUNT;
    }

}
