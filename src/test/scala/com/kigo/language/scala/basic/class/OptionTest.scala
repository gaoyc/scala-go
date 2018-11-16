package com.kigo.language.scala.basic.`class`

import org.junit.Test

/**
  * Created by kigo on 18-3-23.
  */
//@Test
class OptionTest {


  @Test
  def testIterateOption() ={

    val map1 = Map("key1" -> "value1")
    val value1 = map1.get("key1")
    val value2 = map1.get("key2")

    println("test for operation")
    //for操作
    for (x <- value1){
      println(s"value1, $x")
    }

    //map操作
    println("test map operation")
    value1.map(s"val1, Len="+_.length).foreach(println)
    value2.map(s"val2, Len="+_.length).foreach(println)

  }

  @Test
  def testFlatMapOptions(): Unit ={

    // 操作函数返回Option[Int]
    val range = 1 to 6

    // 返回所有元素, 包括Some, None
    val rangeOptions: Seq[Option[Int]] = range.map(i=> if (i%2==0) Some(i) else None)

    // flatten操作, 过滤掉None值
    rangeOptions.flatten.foreach(println)

    // 返回flatMap操作, 过滤掉None, 返回Some[T]的实际数据类型
    val rangeFlatMapOptions:Seq[Int] = range.flatMap(i=> if (i%2==0) Some(i) else None)
    println(s"===========rangeOptions:"+rangeOptions.size+" rangeFlatMapOptions.size:"+rangeFlatMapOptions.size)

    println("after the map function returning the Option[T], the elements are:")
    rangeOptions.foreach(println) // 结果: None Some(2) None Some(4) ...

    println("after the flatMap function returning the Option[T], the elements are:")
    rangeFlatMapOptions.foreach(println) // 结果: 2 4 6

  }



}
