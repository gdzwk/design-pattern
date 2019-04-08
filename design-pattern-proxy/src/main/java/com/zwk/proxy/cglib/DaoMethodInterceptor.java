package com.zwk.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * dao方法拦截
 * @author zwk
 */
public class DaoMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置方法");
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("后置方法");
        return result;
    }

}
