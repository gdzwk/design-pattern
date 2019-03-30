package com.zwk.factorymethod;

import com.zwk.factorymethod.factoryimpl.AppleFactory;
import com.zwk.factorymethod.factoryimpl.OrangeFactory;
import com.zwk.factorymethod.factoryimpl.WatermelonFactory;
import com.zwk.factorymethod.fruitimpl.Apple;
import com.zwk.factorymethod.fruitimpl.Orange;
import com.zwk.factorymethod.fruitimpl.Watermelon;

/**
 * 工厂方法测试类
 * @author zwk
 */
public class FactoryMethodDemo {

    public static void main(String[] args) {
        AbstractFruitFactory appleFactory = new AppleFactory();
        Fruit apple = appleFactory.create();

        AbstractFruitFactory orangeFactory = new OrangeFactory();
        Fruit orange = orangeFactory.create();

        AbstractFruitFactory watermelonFactory = new WatermelonFactory();
        Fruit watermelon = watermelonFactory.create();
    }

}
