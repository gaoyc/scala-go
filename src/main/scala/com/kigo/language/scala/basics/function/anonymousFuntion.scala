package com.kigo.language.scala.basics.function

/**
  * Created by Kigo on 18-5-3.
  * 匿名函数一些示例
  */
object anonymousFuntion {

  def main(args: Array[String]): Unit = {

    /** =================== 匿名函数=================== **/
    //普通二元组参数函数定义
    def fAnoymousDef(t: (Int, Int)): String ={
      (t._1 + t._2).toString
    }


    //定义一个二元组参数的匿名函数/函数字面量
    val fAnoymous = (t: (Int, Int)) => {
      (t._1 + t._2).toString
    }


    /** =================== 函数字面量 =================== **/

    /**
      *Scala提供了多种方法来简化函数字面量中多余的部分，比如前面例子中filter方法中使用的函数字面量,完整的写法如下：
      */

    (x :Int ) => x +1

    /**
      *   首先可以省略到参数的类型，Scala可以根据上下文推算出参数的类型，函数定义可以简化为：
      *  (x) => x + 1
      *   这个函数可以进一步去掉参数的括号，这里的括号不起什么作用：
      *  x => x +1
      */






  }


}
