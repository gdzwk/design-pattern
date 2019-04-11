package com.zwk.strategy.promotions;

/**
 * 优惠活动接口
 * @author zwk
 */
public interface IPromotionStrategy {

    /**
     * 计算实际应付金额
     * @param money 原价
     * @return 优惠后的实际应支付金额
     */
    double pay(double money);

}
