package com.zwk.proxy.customdynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 自定义
 * @author zwk
 */
public class CustomInvocationHandler implements InvocationHandler {

    private Object target;

    public CustomInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置操作");
        Object obj = method.invoke(target, args);
        System.out.println("后置操作");
        return obj;
    }

}
