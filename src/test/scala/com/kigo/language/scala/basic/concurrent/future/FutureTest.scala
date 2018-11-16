package com.kigo.language.scala.basic.concurrent.future

import java.util.concurrent.TimeUnit

import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

/**
  * Created by kigo on 9/22/17.
  */
object FutureTest {

  def invokeFtr(i: Int = 0): Future[String] = future{
    def rand(x: Int) =  scala.util.Random.nextInt(x)
    TimeUnit.SECONDS.sleep(2)
    if (rand(i) > (i/2)) (i+1).toString else throw new Exception
  }


  def testInvokeFtr(){
    val future1 = invokeFtr(5) onSuccess {
      case x => println(s"Got $x")
    }

    val future2 = invokeFtr(5) onFailure  {
      case x => println(s"Got $x")
    }

    val future3 = invokeFtr(5) onComplete   {
      case Success(x) => println(s"Got $x")
      case Failure(e) => println(s"Got failure: ${e.getMessage}" + e)
    }

    println(s"main finish")

    //以同步方法获取结果
    scala.concurrent.Await.result(invokeFtr(5), Duration(10, TimeUnit.SECONDS))



  }

  /**
    * 官方例子: Functional Composition and For-Comprehensions
    */
  def testPurchaseFutureForComprehensions(): Unit ={

    /** ========样例1：传统链式写法 ======= **/
    val rateQuote = Future {
      //connection.getCurrentValue(USD)
      scala.util.Random.nextDouble()
    }

    rateQuote onSuccess { case quote =>
      val purchase = Future {
        if (isProfitable(quote)) connectionBuy(scala.util.Random.nextInt(5), quote)
        else throw new Exception("not profitable")
      }

      purchase onSuccess {
        case amount => println("Purchased " + amount + " USD")
      }
    }

    /** ========样例2: 将上述样例转换为map写法======= **/
    val rateQuote2 = Future {
      scala.util.Random.nextDouble()
    }

    val purchase3 = rateQuote2 map { quote =>
      if (isProfitable(quote)) connectionBuy(5, quote)
      else throw new Exception("not profitable")
    }

    //如果前序rateQuote2发生异常或者失败，purchase3的future同样会包含该异常或者失败.这种扩散语法同样适用于其它combinator操作
    purchase3 onSuccess {
      case amount => println("Purchased " + amount + " USD")
    }

    /** ========样例3: 多个future for用法======= **/
    val usdQuote = Future {
      //connection.getCurrentValue(USD)
      scala.util.Random.nextDouble()
    }
    val chfQuote = Future {
      //connection.getCurrentValue(CHF)
      scala.util.Random.nextDouble()
    }


    //purchase future仅当usdQuote和chfQuote都完成后，且满足指定条件，才能开始执行
    val purchase = for {
    usd <- usdQuote
      chf <- chfQuote
      if isProfitable(usd, chf)
    } yield connectionBuy(scala.util.Random.nextInt(5), chf)

    //purchase执行成功callback回调
    purchase onSuccess {
      case amount => println("Purchased " + amount + " CHF")
    }

    /** ========样例4: 将上述样例使用flatmap翻译 ======= **/
    //The flatMap operation maps its own value into some other future.
    // Once this different future is completed, the resulting future is completed with its value.
    // In our example, flatMap uses the value of the usdQuote future to map the value of the chfQuote into a third future which sends a request to buy a certain amount of Swiss francs. The resulting future purchase is completed only once this third future returned from map completes.
    val purchase2 = usdQuote flatMap {
      usd =>
        chfQuote
          .withFilter(chf => isProfitable(usd, chf))
          .map(chf => connectionBuy(2, chf))
    }

  }

  private def isProfitable(a: Double, b: Double):Boolean ={
    a > b
  }

  private def isProfitable(a: Double):Boolean ={
    a > 0.1
  }

  private def connectionBuy(amount: Double, quote: Double): Double = {
    amount * quote
  }

  def testFutureComposition(): Unit ={

    val tasks = List(3000, 1200, 1800, 600, 250, 1000, 1100, 8000, 550)
    val taskFutures: List[Future[String]] = tasks map { ms =>
      future {
        Thread sleep ms
        s"Task with $ms ms"
      }
    }

    //we want a List[String] as a result. This is done with the Futures companion object.
    val searchFuture: Future[List[String]] = Future sequence taskFutures

    val result = Await result (searchFuture, 2 seconds)
    //However this will throw a TimeoutException, as some of our tasks run more than 2 seconds. Of course we could increase the timeout, but there error could always happen again, when a server is down. Another approach would be to handle the exception and return an error. However all other results would be lost.



  }

  def testFutureForComprehensions(): Unit ={

    //构造futures
    val futures  = (1 to 2).toList.map(i =>{
      val task1: Future[(Int, String, Long)] = Future{
        val start = System.currentTimeMillis()
        TimeUnit.SECONDS.sleep(scala.util.Random.nextInt(5))
        val end = System.currentTimeMillis()
        (i, Thread.currentThread().getName, end- start)
      }
      task1
    })

    /** === for方式多个future连接  **/
    val f1 = futures(0)
    val f2 = futures(1)

    //构造结果future，将会在所有前序future执行完成返回结果后执行
    val parRets = for {
      ftr1 <- f1
      ftr2 <- f2
    } yield (ftr1, ftr2)


    /** ====转换 Future List 成 List Future (map to) ==== **/
    val rets = Future.sequence(futures)
    println(rets)

    //结果future回调函数
    rets onSuccess({
      case ret => println(ret)
    })

    println("begin to sleep to wait the rets")
    TimeUnit.SECONDS.sleep(6)
    println("woke up to be end")

  }


  def main(args: Array[String]): Unit = {

    testFutureForComprehensions

  }

}



object FutureTestEgg extends App{
  val s ="Hello"
  val f:Future[String]= future {
    //TimeUnit.SECONDS.sleep(2)
    s +" future!"
  }
  f onSuccess {
    case msg => println(msg)
  }
  println(s)//不加这句, f onSuccess就不执行
}