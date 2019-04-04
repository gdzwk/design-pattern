package com.zwk.custom;

import lombok.Data;

/**
 * @author zwk
 */
@Data
public class DemoA implements IPrototype {

    private String a1;

    private String a2;

    /**
     * 重写自定义克隆接口，返回指定的原型
     * 将对象的创建及初始化赋值的过程隐藏起来，方便调用者使用
     */
    @Override
    public DemoA prototype() {
        DemoA a = new DemoA();
        a.setA1(this.a1);
        a.setA2(this.a2);
        return a;
    }

}
