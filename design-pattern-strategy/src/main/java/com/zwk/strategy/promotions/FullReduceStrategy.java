package com.zwk.strategy.promotions;

import org.springframework.util.Assert;

/**
 * 满100减10优惠
 * @author zwk
 */
public class FullReduceStrategy implements IPromotionStrategy {

    private static final int FULL_MONEY = 100;

    private static final int REDUCE_MONEY = 10;

    @Override
    public double pay(double money) {
        Assert.isTrue(money > 0, "非法金额");
        if (money >= FULL_MONEY) {
            money -= REDUCE_MONEY;
        }
        return money;
    }

}
