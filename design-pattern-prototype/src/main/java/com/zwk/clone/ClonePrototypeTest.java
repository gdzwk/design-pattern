package com.zwk.clone;

/**
 * 测试 {@link Object#clone()} 自带克隆方法
 * @author zwk
 */
public class ClonePrototypeTest {

    public static void main(String[] args) {
        // 测试 clone() 方法的浅复制效果
        ClonePrototypeTest.shallowClone();

        // 测试重写 clone() 方法的深复制效果
//        ClonePrototypeTest.deepClone();
    }

    /**
     * 测试 {@link Object#clone()} 浅复制
     */
    private static void shallowClone() {
        try {
            DemoC c0 = DemoC.builder().c1("cc").c2("CC").build();
            DemoD d0 = DemoD.builder().d1("dd").d2("DD").demoC(c0).build();

            DemoD d1 = (DemoD) d0.clone();

            System.out.println("d0:" + d0);
            System.out.println("d1:" + d1);
            System.out.println("d0 == d1:" + (d0 == d1));
            System.out.println("d0.demoC == d1.demoC:" + (d0.getDemoC() == d1.getDemoC()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试重写 {@link Object#clone()} 深复制
     */
    private static void deepClone() {
        try {
            DemoC c0 = DemoC.builder().c1("cc").c2("CC").build();
            DemoE e0 = DemoE.builder().e1("dd").e2("DD").demoC(c0).build();

            DemoE e1 = (DemoE) e0.clone();

            System.out.println("e0:" + e0);
            System.out.println("e1:" + e1);
            System.out.println("e0 == e1:" + (e0 == e1));
            System.out.println("e0.demoC == e1.demoC:" + (e0.getDemoC() == e1.getDemoC()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
