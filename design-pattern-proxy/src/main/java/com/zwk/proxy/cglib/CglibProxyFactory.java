package com.zwk.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.util.Assert;

/**
 * cglib代理类工厂
 * @author zwk
 */
public class CglibProxyFactory {

    private CglibProxyFactory() {}

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz, MethodInterceptor interceptor) {
        Assert.isTrue(! clazz.isInterface(), "不能是接口");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(interceptor);
        return (T) enhancer.create();
    }

}