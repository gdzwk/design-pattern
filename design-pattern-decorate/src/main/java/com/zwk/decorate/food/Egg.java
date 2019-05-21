package com.zwk.decorate.food;

import com.zwk.decorate.Pancake;

/**
 * 鸡蛋
 * @author zwk
 */
public class Egg extends Food {

    @Override
    public void addFood(Pancake pancake) {
        pancake.addPrice(1);
        pancake.addMsg("+一个鸡蛋");
    }

}
