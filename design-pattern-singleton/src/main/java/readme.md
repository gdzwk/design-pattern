## 单例模式分类:
* 饿汉式单例
* 懒汉式单例 (双重检测、静态内部类)
* 注册式单例 (IOC)
* 枚举式单例
* ThreadLocal单例


### 1 饿汉式单例
source: com.zwk.hungry.HungrySingleton.java
* 优点: 线程安全，能禁止反射对单例的破坏
* 缺点: 实例提前创建，可能占据多余的空间
* 使用场景: 对内存没高要求的情况

### 2 懒汉式单例 (双重检测)
source: com.zwk.lazy.LazySingleton.java
* 优点: 延迟实例的创建，减少内存空间的占用
* 缺点: 无法避免反射对单例的破坏; synchronized 会影响性能
* 使用场景: 不建议使用

### 3 懒汉式单例 (静态内部类)
source: com.zwk.lazy.LazyInnerSingleton.java
* 优点: 利用jvm的类加载机制，延迟实例的创建，减少内存空间的占用; 避免反射、多线程对单例的破坏
* 缺点: 几乎没有缺点，非说有，那就是写的代码有点多 (和枚举式单例相比较)
* 使用场景: 都适用

### 4 注册式单例
source: com.zwk.register.ContainerSingleton.java
* 优点: 集中管理多个单例实例
* 缺点: synchronized 会有性能问题，但这只是简单的对Spring容器的模拟。
* 使用场景: 容器

### 5 枚举式单例
source: com.zwk.register.EnumerationSingleton.java
* 优点: 使用jvm提供的机制完美避免多线程、反射对单例的破坏; 写法精简
* 缺点: 单例实例提前创建，占用内存空间
* 使用场景: api最优写法

### 6 ThreadLocal单例
source: com.zwk.threadlocal.ThreadLocalSingleton.java
* 优点: 线程级别的单例模式
* 使用场景: 如tomcat中线程上下文