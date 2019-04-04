package com.zwk.register;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器式单例
 * @author zwk
 */
public class ContainerSingleton {

    private static final Map<String, Object> IOC = new ConcurrentHashMap<>(32);

    public static synchronized Object getInstance(String className) {
        Assert.notNull(className, "请输入参数");
        if (! IOC.containsKey(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                IOC.put(className, clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return IOC.get(className);
    }

}
