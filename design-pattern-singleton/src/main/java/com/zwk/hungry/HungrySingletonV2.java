package com.zwk.hungry;

import java.io.*;

/**
 * 饿汉式单例 优化版本2 (修复反射、序列号 对单例模式的破坏)
 * @author zwk
 */
public class HungrySingletonV2 implements Serializable {

    private static final long serialVersionUID = -6156055606532937625L;
    private static final HungrySingletonV2 SINGLETON = new HungrySingletonV2();

    private HungrySingletonV2() {
        if (SINGLETON != null) {
            // 处理反射情况
            throw new RuntimeException("禁止创建实例");
        }
    }

    public static HungrySingletonV2 getInstance() {
        return SINGLETON;
    }

    /**
     * 处理序列化情况 (虽然逻辑上还是会创建2个实例，但最终返回的是同一个实例)
     * @see ObjectStreamClass#invokeReadResolve(Object)
     */
    private Object readResolve() {
        return SINGLETON;
    }

}
