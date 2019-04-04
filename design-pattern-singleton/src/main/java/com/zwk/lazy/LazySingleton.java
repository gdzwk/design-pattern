package com.zwk.lazy;

/**
 * 懒汉式单例 (双锁检测)
 * @author zwk
 */
public class LazySingleton {

    /**
     * 使用 volatile 避免 new 的过程出现指令重排序
     * 导致出现有线程获拿到未完全初始化的单例对象
     */
    private static volatile LazySingleton SINGLETON;

    private LazySingleton() {
        if (SINGLETON != null) {
            throw new RuntimeException("There is already a instance, cannot create again.");
        }
    }

    public static LazySingleton getInstance() throws Exception {
        // 双重锁防止并发阻塞
        if (SINGLETON == null) {
            synchronized (LazySingleton.class) {
                if (SINGLETON == null) {
                    SINGLETON = new LazySingleton();
                }
            }
        }
        return SINGLETON;
    }

}
