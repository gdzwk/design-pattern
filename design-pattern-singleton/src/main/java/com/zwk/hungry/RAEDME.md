# 饿汉式单例

com.zwk.hungry.HungrySingleton
* 优点:
  * 线程安全
  * 能够禁止反射调用
* 缺点:
  * 提前创建实例，可能浪费内存空间