package com.zwk.clone;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwk
 */
@Builder
@Data
public class DemoE implements Cloneable {

    private String e1;

    private String e2;

    private DemoC demoC;

    /**
     * 重写 {@link Object#clone()} 实现深复制
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        DemoE e = (DemoE) super.clone();
        DemoC c = (DemoC) e.getDemoC().clone();
        e.setDemoC(c);
        return e;
    }

}
