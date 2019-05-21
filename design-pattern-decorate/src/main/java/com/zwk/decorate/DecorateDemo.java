package com.zwk.decorate;

import com.zwk.decorate.food.Egg;
import com.zwk.decorate.food.Ham;

public class DecorateDemo {

    public static void main(String[] args) {
        Pancake decorate = new PancakeDecorate(new BasePancake());
        decorate
                .addFood(new Egg())
                .addFood(new Egg())
                .addFood(new Ham());
        System.out.println(decorate.getMsg());
    }

}
