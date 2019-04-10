package com.zwk.delegate;

/**
 * 委托模式测试类
 * @author zwk
 */
public class DelegateTest {

    public static void main(String[] args) {
        Boss boss = new Boss();
        boss.sendCommand("底层架构");
    }

}
