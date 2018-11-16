# 概念术语
- REPL
  >Read，Evalueate(计算),Print, Loop（循环）
- 字面量(literal)  
  >字面量或者字面数据是直接出现在源代码中的数据，如数字5,字母'A'和文本"Hello, World!"  
  
# 安装部署
  

# 执行调试
## REPL加载外部scala代码
  - 命令`:load <file>`
  >从一个外部文件加载和解释scala代码,例如:`scala> :load HelloWorld.scala`
  - "raw"原始模式把代码粘贴到REPL
  >输入`:paste -raw`按回车键，然后粘贴要执行的源代码
  

# Scala类型概述
## scala核心类型的层次体系
  - Any: scala中所有类型的根
  - AnyVal: 所有值类型的根
  - AnyRef: 所有引用(非值)类型的个
  - Nothing: 所有类型的子类
  - Null: 所有指示null值的AnyRef类型的子类
  - Char: Unicode字符
  - Boolean: true或者false
  - String: 字符串(即文本)
  - Unit: 指示没有值
## 类型的操作
  >常用类型操作  
| 操作名 | 示例 | 描述 |  
|--|--|--|  
|`asInstanceOf[type]`|`1.asInstanceOf[Long]`|将一个值转换为指定类型的值。如果这个值与新类型不兼容，会导致一个错误|  
|`getClass`|`(7.0/5).getClass`|返回一个值的类型|  
|`instanceOf`|`(5.0).instanceOf[Float]`|判断值是否指定类型|  
|`to<Type>`|1.toByte|转换函数,可以将一个值转为为一个兼容的值|  
