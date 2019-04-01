package com.zwk.hungry;

import java.io.*;

/**
 * 简单的饿汉式单例
 * @author zwk
 */
public class HungrySingletonV1 implements Serializable {

    private static final long serialVersionUID = -515595096860998788L;
    private static final HungrySingletonV1 SINGLETON = new HungrySingletonV1();

    private HungrySingletonV1() {
    }

    public static HungrySingletonV1 getInstance() {
        return SINGLETON;
    }

}
