# 装饰模式

看大话设计模式中的装饰模式例子的写法，采用的是对象的层层嵌套，逐步装饰。
我感觉这种写法在装饰对象较多时难以阅读，如果采用流式api应该会有更好的体验。

因此Pancake.java中添加了 addFood() 方法，用于进行流式装饰。虽然不知道这是否为标准的
装饰模式，或者写成了其他模式，但目前感觉用的挺好，代码感观也不错。