# 原型模式

原型模式一般用于对现有对象的(浅/深)复制，将复制逻辑隐藏起来，只暴露一个简单的调用方法给使用者。

### com.zwk.custom 
模拟 Object#clone() 方法，自定义一个克隆接口 IPrototype，实现 Object#clone() 的效果
* 浅复制
* 深复制

### com.zwk.clone
测试 Object#clone() 方法的复制效果，默认情况下是浅克隆，对于该对象的堆内存内容直接复制。因此其成员对象仍然引用着同一个。
要实现深复制，需要重写clone()方法。
* 缺点: 深复制需要重写clone()方法，较为麻烦

### com.zwk.serialize
自定义 DeepCloneable 接口，实现深复制效果。
* 优点: 使用简单