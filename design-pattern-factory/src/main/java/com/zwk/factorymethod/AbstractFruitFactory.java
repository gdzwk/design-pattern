package com.zwk.factorymethod;

/**
 * 水果工厂基类
 * @author zwk
 */
public abstract class AbstractFruitFactory {

    /**
     * 生产水果
     * @return 具体的水果 {@link Fruit}
     */
    public abstract Fruit create();

}
