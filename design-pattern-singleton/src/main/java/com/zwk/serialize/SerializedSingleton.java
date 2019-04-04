package com.zwk.serialize;

import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * 防止序列化操作的单例写法
 * @author zwk
 */
public class SerializedSingleton implements Serializable {

    private static final SerializedSingleton SINGLETON = new SerializedSingleton();

    private SerializedSingleton() {}

    public static SerializedSingleton getInstance() {
        return SINGLETON;
    }

    /**
     * 防止序列化返回新对象
     * @see ObjectInputStream#readObject()
     */
    public Object readResolve() {
        return SINGLETON;
    }

}
