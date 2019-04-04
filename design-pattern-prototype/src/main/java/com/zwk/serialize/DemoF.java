package com.zwk.serialize;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwk
 */
@Builder
@Data
public class DemoF implements DeepCloneable {

    private static final long serialVersionUID = 4772967199511537314L;

    private String f1;

    private String f2;

    private DemoE demoE;

}
