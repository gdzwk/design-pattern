package com.zwk.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接对象工厂
 * @author zwk
 */
public class MyConnectionFactory extends BasePooledObjectFactory<Connection> {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Connection create() throws Exception {
        Connection c = new Connection(counter.addAndGet(1));
        System.out.println("----创建新连接:" + c.getId());
        return c;
    }

    @Override
    public PooledObject<Connection> wrap(Connection obj) {
        System.out.println("----包装连接对象:" + obj.getId());
        return new DefaultPooledObject<>(obj);
    }

}
