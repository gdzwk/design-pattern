package com.zwk.decorate;

/**
 * 煎饼
 * @author zwk
 */
public class BasePancake extends Pancake {

    private int price = 5;
    private String msg = "";

    @Override
    public String getMsg() {
        return this.msg + "   煎饼总价为" + this.price();
    }

    @Override
    public int price() {
        return this.price;
    }

    @Override
    public void addPrice(int price) {
        this.price += price;
    }

    @Override
    public void addMsg(String msg) {
        this.msg += msg;
    }

}
