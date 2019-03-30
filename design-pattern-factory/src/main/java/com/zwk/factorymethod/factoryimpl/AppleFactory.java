package com.zwk.factorymethod.factoryimpl;

import com.zwk.factorymethod.AbstractFruitFactory;
import com.zwk.factorymethod.Fruit;
import com.zwk.factorymethod.fruitimpl.Apple;

/**
 * 苹果工厂
 * @author zwk
 */
public class AppleFactory extends AbstractFruitFactory {

    @Override
    public Fruit create() {
        return new Apple();
    }

}
