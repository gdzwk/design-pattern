package com.zwk.proxy.dynamic;

import com.zwk.proxy.DaoImpl;
import com.zwk.proxy.IDao;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

/**
 * 动态代理测试
 * @author zwk
 */
public class DynamicProxyTest {

    public static void main(String[] args) throws Exception {
        IDao dao = (IDao) Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(), new Class[]{IDao.class}, (proxy, method, arg) -> {
            System.out.println("前置处理");
            Object obj = method.invoke(new DaoImpl(), arg);
            System.out.println("后置处理");
            return obj;
        });

//        dao.save();
        dao.get();

        // 把代理类信息输出到class文件中，用于分析学习动态代理
//        byte[] bytes = ProxyGenerator.generateProxyClass("$proxy0", new Class[]{IDao.class});
//        FileOutputStream fos = new FileOutputStream(new File("$proxy0.class"));
//        fos.write(bytes);
//        fos.flush();
//        fos.close();
    }

}
