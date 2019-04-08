package com.zwk.proxy.staticproxy;

import com.zwk.proxy.DaoImpl;

/**
 * 静态代理测试
 * @author zwk
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        StaticDaoProxy proxy = new StaticDaoProxy();
        proxy.setDao(new DaoImpl());

        proxy.save();
    }

}
