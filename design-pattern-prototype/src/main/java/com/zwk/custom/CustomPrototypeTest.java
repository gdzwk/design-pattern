package com.zwk.custom;

/**
 * 自定义原型测试类
 * @author zwk
 */
public class CustomPrototypeTest {

    public static void main(String[] args) {
        // 浅克隆测试
//        CustomPrototypeTest.shallowClone();

        // 深克隆测试
        CustomPrototypeTest.deepClone();
    }

    /**
     * 浅克隆测试
     */
    private static void shallowClone() {
        DemoA a0 = new DemoA();
        a0.setA1("aa");
        a0.setA2("AA");

        DemoA a1 = a0.prototype();

        System.out.println(a0);
        System.out.println(a1);
        System.out.println(a0 == a1);
    }

    /**
     * 深克隆测试
     */
    private static void deepClone() {
        DemoA a0 = new DemoA();
        a0.setA1("aa");
        a0.setA2("AA");
        DemoB b0 = new DemoB();
        b0.setB1("bb");
        b0.setB2("BB");
        b0.setDemoA(a0);

        DemoB b1 = b0.prototype();

        System.out.println(b0);
        System.out.println(b1);
        System.out.println(b0 == b1);
        System.out.println(b0.getDemoA() == b1.getDemoA());
    }

}
