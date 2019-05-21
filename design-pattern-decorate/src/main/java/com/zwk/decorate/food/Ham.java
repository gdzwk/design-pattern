package com.zwk.decorate.food;

import com.zwk.decorate.Pancake;

/**
 * 火腿
 * @author zwk
 */
public class Ham extends Food {

    @Override
    public void addFood(Pancake pancake) {
        pancake.addPrice(2);
        pancake.addMsg("+一个火腿");
    }

}
