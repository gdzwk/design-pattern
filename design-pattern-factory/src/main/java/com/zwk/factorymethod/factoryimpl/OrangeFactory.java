package com.zwk.factorymethod.factoryimpl;

import com.zwk.factorymethod.AbstractFruitFactory;
import com.zwk.factorymethod.Fruit;
import com.zwk.factorymethod.fruitimpl.Orange;

/**
 * 橘子工厂
 * @author zwk
 */
public class OrangeFactory extends AbstractFruitFactory {

    @Override
    public Fruit create() {
        return new Orange();
    }

}
