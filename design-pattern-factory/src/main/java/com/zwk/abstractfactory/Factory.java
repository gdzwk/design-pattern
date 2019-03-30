package com.zwk.abstractfactory;

/**
 * 工厂基类
 * @author zwk
 */
public interface Factory {

    /**
     * 生产水果
     */
    default Fruit createFruit() {
        return null;
    }

    /**
     * 生产肉类
     */
    default Meat createMeat() {
        return null;
    }

}
