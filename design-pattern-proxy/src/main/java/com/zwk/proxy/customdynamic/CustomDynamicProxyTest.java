package com.zwk.proxy.customdynamic;

import com.zwk.proxy.DaoImpl;
import com.zwk.proxy.IDao;

/**
 * 仿写动态代理模式测试
 * @author zwk
 */
public class CustomDynamicProxyTest {

    public static void main(String[] args) throws Exception {
        IDao proxy = CustomDynamicProxy.newProxyInstance(
                // 类加载器
                CustomDynamicProxyTest.class.getClassLoader(),
                // 实现接口
                IDao.class,
                // 自定义代理方法的前置、后置处理
                new CustomInvocationHandler(new DaoImpl()));
        proxy.save();
    }

}
