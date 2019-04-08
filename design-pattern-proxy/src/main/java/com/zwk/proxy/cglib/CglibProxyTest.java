package com.zwk.proxy.cglib;

import com.zwk.proxy.DaoImpl;
import com.zwk.proxy.IDao;
import org.springframework.cglib.core.DebuggingClassWriter;

/**
 * cglib代理测试
 * @author zwk
 */
public class CglibProxyTest {

    public static void main(String[] args) throws Exception {
        // 输出cglib代理类class，查看代理类结构
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, CglibProxyTest.class.getResource("").getPath());

        DaoImpl dao = CglibProxyFactory.getInstance(DaoImpl.class, new DaoMethodInterceptor());
        dao.save();
    }

}
