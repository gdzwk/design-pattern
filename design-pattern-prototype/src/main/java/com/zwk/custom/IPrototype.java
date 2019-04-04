package com.zwk.custom;

/**
 * 自定义克隆接口
 * @author zwk
 */
public interface IPrototype {

    /**
     * 自定义克隆方法
     * @return 克隆对象
     */
    default Object prototype() {
        return null;
    }

}
