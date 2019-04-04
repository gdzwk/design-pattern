package com.zwk.lazy;

/**
 * 懒汉式单例 (使用静态内部类)
 * @author zwk
 */
public class LazyInnerSingleton {

    private LazyInnerSingleton() {
        if (LazyHolder.SINGLETON != null) {
            throw new RuntimeException("不能重复创建实例");
        }
    }

    public static LazyInnerSingleton getInstance() {
        return LazyHolder.SINGLETON;
    }

    private static class LazyHolder {
        private static final LazyInnerSingleton SINGLETON = new LazyInnerSingleton();
    }

}
