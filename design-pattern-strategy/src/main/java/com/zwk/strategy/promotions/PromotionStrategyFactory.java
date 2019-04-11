package com.zwk.strategy.promotions;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠策略工厂
 * @autuhor zwk
 */
public class PromotionStrategyFactory {

    private static final Map<String, IPromotionStrategy> PROMOTION_STRATEGY_MAP = new HashMap<>(16);

    /**
     * 无优惠策略
     */
    private static final IPromotionStrategy NONE_PROMOTION_STRATEGY = new NonePromotionStrategy();

    static {
        PROMOTION_STRATEGY_MAP.put("fullReduce", new FullReduceStrategy());
        PROMOTION_STRATEGY_MAP.put("discount", new DiscountStrategy());
    }

    private PromotionStrategyFactory() {}

    /**
     * 获取优惠策略
     * @param promotionName 优惠策略名称
     * @return 优惠策略
     */
    public static IPromotionStrategy getPromotionStrategy(String promotionName) {
        if (StringUtils.isEmpty(promotionName)) {
            return NONE_PROMOTION_STRATEGY;
        }
        IPromotionStrategy strategy = PROMOTION_STRATEGY_MAP.get(promotionName);
        return strategy != null ? strategy : NONE_PROMOTION_STRATEGY;
    }

}
