package com.zwk.abstractfactory.factoryimpl;

import com.zwk.abstractfactory.Factory;
import com.zwk.abstractfactory.Fruit;
import com.zwk.abstractfactory.Meat;
import com.zwk.abstractfactory.fruitImpl.Apple;
import com.zwk.abstractfactory.meatimpl.Chicken;

/**
 * 工厂实例A
 * @author zwk
 */
public class FactoryA implements Factory {

    @Override
    public Fruit createFruit() {
        return new Apple();
    }

    @Override
    public Meat createMeat() {
        return new Chicken();
    }

}
