com.zwk.register.ContainerSingleton 容器式单例
* 模仿Spring容器，优点在于可管理多个单例实例

com.zwk.register.EnumerationSingleton 枚举式单例
* 利用jvm底层提供的机制，完美避免多线程、反射、序列化操作对单例的破坏
* 写法精简