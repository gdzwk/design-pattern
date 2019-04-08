# 代理模式练习

## 1 静态代理模式
**com.zwk.proxy.staticproxy**
静态代理模式需要以下信息:
1. 实现接口的被代理类
2. 实现接口的代理类
3. 代理类持有被代理类
静态代理模式使用简单，但不符合开闭原则，不易于扩展。需要显式创建代理类。

## 2 动态代理模式
**com.zwk.proxy.dynamic**
动态代理模式不需要显式创建代理类，根据业务需求动态生成class字节码，并直接加载到jvm中形成对应的class类
* 缺点:
  * 代理类通过反射调用被代理类的方法，损耗性能
  * 被代理类必须实现接口


## 3 仿写动态代理模式
**com.zwk.proxy.customdynamic**
对动态代理模式的模仿需要以下几个步骤:
1. 分析动态代理模式中生成的代理类类结构
2. 指定一个被代理类，仿写一个代理类
3. 使用java代码调用编译器编译java文件 -> 加载类class
4. 调用代理类方法

* 在写创建java文本时，发现方法返回值void使用
  * (method.getReturnType() == Void.class) -> false
  * (method.getReturnType() == Void.TYPE) -> true
  * 所有的包装器类均有一个 TYPE 常量


## 4 cglib代理
**com.zwk.proxy.cglib**
cglib代理与动态代理功能类似，可以动态生成class字节码，并直接加载到jvm中形成对应的class类
与动态代理的区别在于:
* cglib底层的字节码操作使用了asm框架
* cglib代理的对象可不实现任何接口
* 将反射调用改为为直接方法调用，避免了反射调用的效率损耗