package com.zwk.threadlocal;

/**
 * 线程单例
 * @author zwk
 */
public class ThreadLocalSingleton {

    private static final ThreadLocal<ThreadLocalSingleton> LOCAL = ThreadLocal.withInitial(ThreadLocalSingleton::new);

    private ThreadLocalSingleton() {}

    public static ThreadLocalSingleton getInstance() {
        return LOCAL.get();
    }

}
