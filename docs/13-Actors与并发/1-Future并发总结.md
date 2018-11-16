
# 总结
1. 资料汇总
  - 官方参考文档  
    *在线*  
  http://docs.scala-lang.org/overviews/core/futures.html  
    *本地:*  
  /mnt/data/COMPANY/HZ/文档资料/Scala/officer/document/a-Guides and Overviews

## 要点总结
1. for-comprehension(for表达式)并发执行future
  - futures在for表达式前创建.多个future(s)都是在使用使用for-comprehension(for表达式)前创建的，一旦创建，无法阻止其被执行，至于什么时候执行取决于execution context，可以认为是并发执行(并发提交，实际是否能并发执行取决与底层线程池执行器)
  - 第二种，future(s)是在for表达式内部创建的，后序的future仅仅仅是在前序futur执行完成且返回结果后，才能被执行，因此可认为是顺序的执行方式，不能并发执行。
  - for表达式是仅仅是一种 "map", "flatMap" and "withFilter"方法的语法糖(syntactic sugar)


## 样例总结
- Scala 中的 Future 并发模式
一切皆 Future:
```
val mFuture = future {
  Thread sleep 1000
  "result"
}
val result = Await result (mFuture, 3 seconds)
```
  - Callback 的几种方式：
```  
f.onComplete {
  case Success(result) =>
  case Failure(ex) =>
}

Await result (mFuture, 5 seconds)
```
  - Timeout Fallback:
```
val searchFuture = search(worktime)
val fallback = after(timeout, context.system.scheduler) {
  Future successful s"$worktime ms > $timeout"
}
Future firstCompletedOf Seq(searchFuture, fallback)
```

  - Future 之后的运算 map / filter 成其他 Future：
```  
f.map { r1 =>
    ...
    r2
}
```
  - 多个 Future 链的处理 flatMap:
```  
val nestedFuture: Future[Future[Boolean]] = heatWater(Water(25)).map {
  water => temperatureOkay(water)
}
val flatFuture: Future[Boolean] = heatWater(Water(25)).flatMap {
  water => temperatureOkay(water)
}
```
  - 多个 Future 的链合并的另一种方式：
```  
val f = for {
  result1 <- remoteCall1
  result2 <-  remoteCall2
} yield List(result1, result2)
```
假如 future 定义在 for 之前则会并发执行，否则会顺序执行。另外，假如顺序执行 result1 可以作为参数传递到 remoteCall2 中。

  - 转换 Future List 成 List Future (map to)
`Future.sequence(list) //(并发执行)`
  - 转换 Future List 成单个 Future (map to)
`Future.reduce(list)(f)`
Option 和 getOrElse 经常用在 Future 执行中。

  - Scala 中的并发任务执行体 ExecutionContext

Scala 中的 ExecutionContext 和 Java 的线程池的概念非常相似。都是执行具体 Task 的执行体。
Scala 中默认的线程池：
`import scala.concurrent.ExecutionContext.Implicits.global`
是最方便的做法，如果不考虑优化和性能，在所有需要 ExecutionContext 的地方引用即可。当然这在生产环境是行不通的，原因是假如有 Task Block 了整个 global 线程池，应用将变得不可响应，即使 Block 不一定发生在本应用中，比如数据库的操作引起的 blocking 。  
  *可以修改默认线程池的大小*
-Dscala.concurrent.context.numThreads=8 -Dscala.concurrent.context.maxThreads=8
  *自定义线程池*
     创建固定大小线程池
     `implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))`
假如熟悉 Java 线程池的话，线程池的创建和 Java 中完全一样，可以套用。

  - Scala & Akka 中的 Dispatcher
    - 定义一个 Dispatcher：
```
my-dispatcher {
  # Dispatcher is the name of the event-based dispatcher
  type = Dispatcher
  # What kind of ExecutionService to use
  executor = "fork-join-executor"
  # Configuration for the fork join pool
  fork-join-executor {
    # Min number of threads to cap factor-based parallelism number to
    parallelism-min = 2
    # Parallelism (threads) ... ceil(available processors * factor)
    parallelism-factor = 2.0
    # Max number of threads to cap factor-based parallelism number to
    parallelism-max = 10
  }
  # Throughput defines the maximum number of messages to be
  # processed per actor before the thread jumps to the next actor.
  # Set to 1 for as fair as possible.
  throughput = 100
}
```
或者这样：
```
my-thread-pool-dispatcher {
  # Dispatcher is the name of the event-based dispatcher
  type = Dispatcher
  # What kind of ExecutionService to use
  executor = "thread-pool-executor"
  # Configuration for the thread pool
  thread-pool-executor {
    # minimum number of threads to cap factor-based core number to
    core-pool-size-min = 2
    # No of core threads ... ceil(available processors * factor)
    core-pool-size-factor = 2.0
    # maximum number of threads to cap factor-based number to
    core-pool-size-max = 10
  }
  # Throughput defines the maximum number of messages to be
  # processed per actor before the thread jumps to the next actor.
  # Set to 1 for as fair as possible.
  throughput = 100
}
```
在 Scala 代码中引用引用之前定义的 Dispatcher：

implicit val executionContext = system.dispatchers.lookup("my-dispatcher")
给某个 Actor 指定 dispatcher：

val myActor = context.actorOf(Props[MyActor].withDispatcher("my-dispatcher"), "myactor1")
最佳实践

将不同类型的运算进行 dispatcher 隔离：
给 blocking I/O 创建单独的线程池：

因为 JDBC 没有 non-blocking API，所以为 DB R/W Heavey R/W 这些 Block 操作创建单独的 dispatcher。并在不同的 dispatcher 中执行不同类型的 Future 计算。

object Contexts {
  implicit val simpleDbLookups: ExecutionContext = Akka.system.dispatchers.lookup("contexts.simple-db-lookups")
  implicit val expensiveDbLookups: ExecutionContext = Akka.system.dispatchers.lookup("contexts.expensive-db-lookups")
  implicit val dbWriteOperations: ExecutionContext = Akka.system.dispatchers.lookup("contexts.db-write-operations")
  implicit val expensiveCpuOperations: ExecutionContext = Akka.system.dispatchers.lookup("contexts.expensive-cpu-operations")
}
同理，需要给 CPU 密集计算创建单独的线程池。

更多相关链接

http://doc.akka.io/docs/akka/snapshot/scala/dispatchers.html 
https://www.playframework.com/documentation/2.4.x/ThreadPools 
http://stackoverflow.com/questions/26593989/how-to-achieve-high-concurrency-with-spray-io-in-this-future-and-thread-sleep-ex 
http://docs.scala-lang.org/overviews/core/futures.html 
https://github.com/alexandru/scala-best-practices/blob/master/sections/4-concurrency-parallelism.md 
http://www.javacodegeeks.com/2013/08/future-composition-with-scala-and-akka.html 
http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html 
http://nerd.kelseyinnis.com/blog/2013/11/12/idiomatic-scala-the-for-comprehension/ 
http://loicdescotte.github.io/posts/scala-compose-option-future/ 
http://buransky.com/scala/scala-for-comprehension-with-concurrently-running-futures/



## Execution Context
### The Global Execution Context

```

implicit val ec = ExecutionContext.global

for( i <- 1 to 32000 ) {
  Future {
    blocking {
      Thread.sleep(999999)		
    }
  }
}

```

### Adapting a Java Executor
   
ExecutionContext.fromExecutor(new ThreadPoolExecutor( /* your configuration */ ))


# 资料参考
## 链式future的理解

1. for-comprehension两种链式future的区别（for表达式并发执行future）
- **Version 1:**
```
val milkFuture = future { getMilk() }
val flourFuture = future { getFlour() }

for {
  milk <- milkFuture
  flour <- flourFuture
} yield (milk + flour)
```

- **Version 2:**
```
for {
  milk <- future { getMilk() }
  flour <- future { getFlour() }
} yield (milk + flour)
```

- **区别**
  - 第一种的两个future都是在使用使用for-comprehension(for表达式)前创建的，一旦创建，无法阻止其被执行，至于什么时候执行取决于execution context，可以认为是并发执行(并发提交，实际是否能并发执行取决与底层线程池执行器)
  - 第二种，getFlour仅是在getMilk执行完成且返回结果后，才能被执行，因此可认为是顺序的执行方式，不能并发执行。

2. Question:对下面链式future的理解
```
for {
   r1 <- future1
   r2 <- future2
   r3 <- future3
} yield (r1+r2+r3)
```
**Ans:**
  >First about for comprehension. It was answered on SO many many times, that it's an abstraction over a couple of monadic operations: map, flatMap, withFilter. When you use <-, scalac desugars this lines into monadic flatMap:

`r <- monad into monad.flatMap(r => ... )`

  > it looks like an imperative computation (what a monad is all about), you bind a computation result to the r. And yield part is desugared into map call. Result type depends on the type of monad's.

  > Future trait has a flatMap and map functions, so we can use for comprehension with it. In your example can be desugared into the following code:

`future1.flatMap(r1 => future2.flatMap(r2 => future3.map(r3 => r1 + r2 + r3) ) )`

- Parallelism aside
  It goes without saying that if execution of future2 depends on r1 then you can't escape sequential execution, but if the future computations are independent, you have two choices. You can enforce sequential execution, or allow for parallel execution. You can't enforce the latter, as the execution context will handle this.
```
//该方式，future内部顺序执行
val res = for {
   r1 <- computationReturningFuture1(...)
   r2 <- computationReturningFuture2(...)
   r3 <- computationReturningFuture3(...)
} yield (r1+r2+r3)
```
will always run sequentailly. It can be easily explained by the desugaring, after which the subsequent computationReturningFutureX calls are only invoked inside of the flatMaps.
```
val future1 = computationReturningFuture1(...)
val future2 = computationReturningFuture2(...)
val future3 = computationReturningFuture3(...)
//该方式，future内部会并发执行，直至所有执行完成返回结果
val res = for {
   r1 <- future1
   r2 <- future2
   r3 <- future3
} yield (r1+r2+r3)
is able to run in parallel and the for comprehension aggregates the results.
```
