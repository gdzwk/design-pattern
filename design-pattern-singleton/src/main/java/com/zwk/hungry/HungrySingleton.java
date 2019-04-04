package com.zwk.hungry;

/**
 * 饿汉式单例 优化版本2 (修复反射、序列号 对单例模式的破坏)
 * @author zwk
 */
public class HungrySingleton {

    private static final HungrySingleton SINGLETON = new HungrySingleton();

    private HungrySingleton() {
        if (SINGLETON != null) {
            // 处理反射情况
            throw new RuntimeException("禁止创建实例");
        }
    }

    public static HungrySingleton getInstance() {
        return SINGLETON;
    }

}
