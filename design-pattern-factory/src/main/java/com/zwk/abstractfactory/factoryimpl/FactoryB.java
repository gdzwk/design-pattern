package com.zwk.abstractfactory.factoryimpl;

import com.zwk.abstractfactory.Factory;
import com.zwk.abstractfactory.Fruit;
import com.zwk.abstractfactory.Meat;
import com.zwk.abstractfactory.fruitImpl.Apple;
import com.zwk.abstractfactory.fruitImpl.Orange;
import com.zwk.abstractfactory.meatimpl.Beef;
import com.zwk.abstractfactory.meatimpl.Chicken;

/**
 * 工厂实例B
 * @author zwk
 */
public class FactoryB implements Factory {

    @Override
    public Fruit createFruit() {
        return new Orange();
    }

    @Override
    public Meat createMeat() {
        return new Beef();
    }

}
