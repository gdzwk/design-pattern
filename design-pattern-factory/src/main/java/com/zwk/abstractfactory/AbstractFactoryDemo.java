package com.zwk.abstractfactory;

import com.zwk.abstractfactory.factoryimpl.FactoryA;
import com.zwk.abstractfactory.factoryimpl.FactoryB;

/**
 * 抽象工厂测试类
 * @author zwk
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        Factory factoryA = new FactoryA();
        factoryA.createFruit();
        factoryA.createMeat();

        Factory factoryB = new FactoryB();
        factoryB.createFruit();
        factoryB.createMeat();
    }

}
