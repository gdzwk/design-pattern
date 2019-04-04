package com.zwk.serialize;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwk
 */
@Builder
@Data
public class DemoE implements DeepCloneable {

    private static final long serialVersionUID = 3628052592842527106L;

    private String e1;

    private String e2;

}
