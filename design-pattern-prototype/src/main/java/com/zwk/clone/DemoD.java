package com.zwk.clone;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwk
 */
@Builder
@Data
public class DemoD implements Cloneable {

    private String d1;

    private String d2;

    private DemoC demoC;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
