package com.zwk.decorate;

import com.zwk.decorate.food.Food;

/**
 * @author zwk
 */
public class PancakeDecorate extends Pancake {

    private Pancake pancake;

    public PancakeDecorate(Pancake pancake) {
        this.pancake = pancake;
    }

    @Override
    public String getMsg() {
        return pancake.getMsg();
    }

    @Override
    public int price() {
        return pancake.price();
    }

    @Override
    public void addPrice(int price) {
        pancake.addPrice(price);
    }

    @Override
    public void addMsg(String msg) {
        pancake.addMsg(msg);
    }

    @Override
    public Pancake addFood(Food food) {
        food.addFood(this.pancake);
        return this;
    }

}
