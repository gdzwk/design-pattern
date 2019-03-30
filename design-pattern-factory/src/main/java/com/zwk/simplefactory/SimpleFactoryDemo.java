package com.zwk.simplefactory;

/**
 * 简单工厂测试类
 * @author zwk
 */
public class SimpleFactoryDemo {

    public static void main(String[] args) {
        Fruit apple = FruitFactory.create('a');
        Fruit orange = FruitFactory.create('o');
        Fruit watermelon = FruitFactory.create('w');
    }

}
