package com.zwk.hungry;

import java.io.*;

/**
 * 饿汉式单例测试
 */
public class HungrySingleDemo {

    public static void main(String[] args) {
        // 测试 V1 版本的饿汉式单例
        testV1();

        // 测试 V2版本的饿汉式单例
//        testV2();
    }

    public static void testV1() {
        // 正常情况下使用
        HungrySingletonV1 s1 = HungrySingletonV1.getInstance();
        HungrySingletonV1 s2 = HungrySingletonV1.getInstance();
        System.out.println(s1 == s2);

        // 单线程+反射 (破坏单例模式)
        try {
            HungrySingletonV1 s3 = HungrySingletonV1.class.getDeclaredConstructor(null).newInstance();
            System.out.println(s1 == s3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 单线程+序列化 (破坏单例模式)
        try {
            File file = new File("./HungrySingleton.txt");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(s1);
            oos.flush();
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            HungrySingletonV1 s4 = (HungrySingletonV1) ois.readObject();
            System.out.println(s1 == s4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testV2() {
        // 正常情况下使用
        HungrySingletonV2 s1 = HungrySingletonV2.getInstance();
        HungrySingletonV2 s2 = HungrySingletonV2.getInstance();
        System.out.println(s1 == s2);

        // 单线程+反射
        try {
            HungrySingletonV2 s3 = HungrySingletonV2.class.getDeclaredConstructor(null).newInstance();
            System.out.println(s1 == s3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 单线程+序列化
        try {
            File file = new File("./HungrySingleton.txt");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(s1);
            oos.flush();
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            HungrySingletonV2 s4 = (HungrySingletonV2) ois.readObject();
            System.out.println(s1 == s4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
