Object/对象
=======
# 语法:定义对象
`object <identifier> [extends <identifier>] [{ fields, method, and classes }]`
# 要点总结
1. 对象(object)是一个类类型，只能有不超过1个实例，即单例模式
2. 对象和类没有完全解耦合，对象可以扩张一个类，从而可以在一个全局实例中使用它的字段和方法，反之而不行