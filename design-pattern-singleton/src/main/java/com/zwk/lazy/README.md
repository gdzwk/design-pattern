# 懒汉式单例

com.zwk.lazy.LazySingleton  饿汉式单例 (双锁检测)
* 缺点
  * 无法禁止反射的调用
  * 使用 synchronized 存在性能问题
  
com.zwk.lazy.LazyInnerSingleton  饿汉式单例 (静态内部类)
* 优点
  * 完美使用了jvm的类加载机制，具有延迟加载效果
  * 避免了 synchronized 的性能问题
  * 能够禁止反射调用
