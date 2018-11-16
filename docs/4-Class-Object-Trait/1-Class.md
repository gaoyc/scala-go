Class
=======
# Case类
## 总结
1. 语法:定义一个case类
`
case class <identifier> ([var] <identifier>: <type>[, ... ]) [extends <identifier>(<input parameters>)] [{ fields and methods }]
`
2. case class是不可实例化的类，包括多个自动生产的方法，还包括一个自动生成的**伴生对象**
3. 对数据传输对象很适用
4. 生产的case类方法

|方法名|位置|描述|
|-|-|-|
|apply|对象|这是一个工厂方法，用于实例化case类|
|copy|类|返回实例的一个副本，并完成所需的修改。参数是类的字段，默认值设置为当前字段值|
|equals|类|如另个实例中所有字段与这个实例的中所有字段匹配则返回true|
|hashCode|类|返回实例字段的一个散列值|
|toString|类||
|unapply|类|将实例抽取到一个字段元组，从而可以使用case类实例完成模式匹配|
--------
  - apply