# 工厂方法模式练习

简单工厂模式将对象的创建与使用进行了分离。降低了客户端的使用难度。
但是缺点也很明显，不符合开闭原则、单一职责原则。

# 1. 模式结构
* AbstractFruitFactory 工厂基类
* AppleFactory / OrangeFactory / WatermelonFactory 工厂实现
* Fruit 水果基类
* Apple / Orange / Watermelon 水果实现

# 2. 工厂方法模式优缺点

优点
* 和简单工厂模式相比较，工厂职责更单一，且易于扩展

缺点
* 业务复杂情况下，扩展需要添加更多的类
* 客户端需要了解具体的工厂实现类，才能进行准确的调用
* 仅用于同一类的产品，单一维度