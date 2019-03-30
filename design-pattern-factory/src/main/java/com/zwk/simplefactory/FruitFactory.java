package com.zwk.simplefactory;

import com.zwk.simplefactory.fruitimpl.Apple;
import com.zwk.simplefactory.fruitimpl.Orange;
import com.zwk.simplefactory.fruitimpl.Watermelon;

/**
 * 水果工厂
 * @author zwk
 */
public class FruitFactory {

    /**
     * 获取指定的水果
     * @param fruitName 水果参数 (Apple:"a"  Orange:"o"  Watermelon:"w")
     */
    public static Fruit create(char fruitName) {
        Fruit fruit = null;
        switch (fruitName) {
            case 'a' :
                fruit = new Apple();
                break;
            case 'o' :
                fruit = new Orange();
                break;
            case 'w' :
                fruit = new Watermelon();
                break;
            default :
                break;
        }
        return fruit;
    }

}
