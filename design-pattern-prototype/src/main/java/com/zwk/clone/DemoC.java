package com.zwk.clone;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwk
 */
@Builder
@Data
public class DemoC implements Cloneable {

    private String c1;

    private String c2;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
