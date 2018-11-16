总结
=

# 要点
## printf格式化常用字符
  - %c	字符
  - %d	十进制数字
  - %e  指数浮点数
  - %i	整数(十进制)
  - %o	八进制数字
  - %f  浮点数
  - %s	字符串
  - %u	无符号十进制数字
  - %x  十六进制
  - %%  打印一个百分号
  - \\%  打印一个百分号
  
## 字符串中的查找模式, 替换模式, 模式匹配的部分 
   *语法:用正则表达式捕获值*
   `val <Regex value>(<identifier>) = <input string>`
  - 模式查找替换，在一个String上调用.r就可以创建一个Regrex对象，之后在查找中是否包含一个匹配时，就可以用findFirstIn, 当需要查找完全匹配是就用findAllIn  
  - 模式匹配的部分,即抽取一个或者多个字符串中正则匹配到的部分  

  *Egg*  
  ```scala
  val numPattern = "[0-9]+".r
  val address = "123 Main Street Suite 101"
  val match1 =  numPattern.findFirstIn(address) //查找模式
  // match1: Option[String] =  Some(123)
  val matchAll = numPattern.findAllIn(address)
  // 替换模式
  val regex = "[0-9]".r
  val newAddress = regex.replaceAllIn(address, "newStrPlaceHolder")
  // 抽取一个或者多个字符串中正则匹配到的部分
  val pattern = "([0-9]+)([a-zA-Z]+)".r
  val pattern(count, fruit) = "100 Bananas"
  ```

