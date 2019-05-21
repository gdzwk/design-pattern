package com.zwk.decorate;

import com.zwk.decorate.food.Food;

/**
 * 煎饼
 * @author zwk
 */
public abstract class Pancake {

    public abstract String getMsg();

    public abstract int price();

    public abstract void addPrice(int price);

    public abstract void addMsg(String msg);

    public Pancake addFood(Food food) {
        return this;
    }

}
