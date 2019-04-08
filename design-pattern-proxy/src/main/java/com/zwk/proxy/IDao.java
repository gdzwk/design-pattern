package com.zwk.proxy;

/**
 * dao接口
 * @author zwk
 */
public interface IDao {

    /**
     * 保存信息
     */
    void save();

    default void get() {
        System.out.println("获取信息");
    }

}
