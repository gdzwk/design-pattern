package com.zwk.serialize;

/**
 * 测试序列化/反序列化克隆
 * @author zwk
 */
public class SerializePrototypeTest {

    public static void main(String[] args) {
        // 测试序列化/反序列化深复制效果
        SerializePrototypeTest.deepClone();
    }

    /**
     * 测试序列化/反序列化深复制效果
     */
    private static void deepClone() {
        DemoE e0 = DemoE.builder().e1("ee").e2("EE").build();
        DemoF f0 = DemoF.builder().f1("ff").f2("FF").demoE(e0).build();

        DemoF f1 = (DemoF) f0.deepClone();
        DemoE e1 = f1.getDemoE();

        System.out.println("f0:" + f0);
        System.out.println("f1:" + f1);
        System.out.println("f0 == f1:" + (f0 == f1));
        System.out.println("f0.e == f1.e" + (f0.getDemoE() == f1.getDemoE()));
    }

}
