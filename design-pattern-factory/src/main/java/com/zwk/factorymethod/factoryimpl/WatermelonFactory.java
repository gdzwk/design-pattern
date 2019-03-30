package com.zwk.factorymethod.factoryimpl;

import com.zwk.factorymethod.AbstractFruitFactory;
import com.zwk.factorymethod.Fruit;
import com.zwk.factorymethod.fruitimpl.Watermelon;

/**
 * 西瓜工厂
 * @author zwk
 */
public class WatermelonFactory extends AbstractFruitFactory {

    @Override
    public Fruit create() {
        return new Watermelon();
    }

}
