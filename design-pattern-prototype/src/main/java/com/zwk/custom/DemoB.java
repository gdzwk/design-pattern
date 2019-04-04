package com.zwk.custom;

import lombok.Data;

/**
 * @author zwk
 */
@Data
public class DemoB implements IPrototype {

    private String b1;

    private String b2;

    private DemoA demoA;

    /**
     * 重写自定义克隆方法，实现深克隆
     * @see DemoA
     */
    @Override
    public DemoB prototype() {
        DemoB b = new DemoB();
        b.setB1(this.b1);
        b.setB2(this.b2);
        if (this.demoA != null) {
            b.setDemoA(this.demoA.prototype());
        }
        return b;
    }

}
