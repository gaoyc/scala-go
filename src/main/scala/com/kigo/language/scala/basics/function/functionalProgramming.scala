package com.kigo.language.scala.basics.function

/**
  *
  * Created by Kigo on 18-5-3.
  * 函数高阶编程（匿名函数的使用，函数作为参数，返回值进行传递）
  */

object functionalProgramming {
  def main(args: Array[String]): Unit = {
    /**
      * 函数和变量一样可以直接赋值给变量
      *
      */
    val hiData = hiBigData _
    hiData("Spark")

    /**
      * 函数更常使用的是匿名函数，定义的时候只需要说明输入参数的类型和函数体即可，不需要名称
      * 如果你要是用的话，一般会把这个匿名函数赋值给一个变量（其实是val常量）
      * 表现形式——（传入参数）=>{方法体}
      */
    val f = (name:String)=>println("Hi,"+name)
    f("kafka")

    /**
      * 函数也可以作为一个参数传给函数，这极大的简化了编程的语法
      * 以前Java的方式是new一个接口实例，并且在接口实例的回调方法callback中来实现业务逻辑，
      * 这种方式是高阶函数编程方式
      * 函数作为参数的表现形式——函数名:传入参数*=>返回值
      */
    def getName(func:String=>Unit,name:String){
      func(name)
    }
    getName(f,"Scala")
    /**

      * 把数组中每个数乘以2再打印出来
      */
    Array(1 to 10:_*).map { item:Int => item*2 }.foreach { x => println(x) }
    /**
      * 函数的返回值也可以是函数
      * 下面是函数的返回值是函数的列子，这里面表明Scala实现了闭包
      * 闭包的内幕：Scala的函数背后是类和对象，所以Scala的参数都作为了对象的成员，后续可以继续访问，这就是其闭包的原理
      *
      * currying,复杂的函数式编程中经常使用，可以维护变量在内存中的状态，且实现返回函数的链式功能，可以实现非常复杂的算法和逻辑
      */
    def funcResult = (name:String)=>println(name)
    funcResult("java")
    //currying函数写法
    def funcResult1(message:String) = (name:String)=>println(message+" : "+name)
    //原先写法 def fimcResult1(message:String,name:String){println(message+" : "+name)}
    funcResult1("Hello")("Java")
    val result = funcResult1("Hello")//与前面的调用方法相同
    result("java")


  }

  def hiBigData(name:String){
    println("Hi,"+name)
  }
}
