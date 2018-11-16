Trait
=======
# 要点总结
1. 语法:  
   `trait <identifier> [extends <identifier>] [{ fields, methods, and class}]`  
2. trait支持多重继承的类. 类, case类，对象以及trait都只能扩张至多一个类，但是可以同时扩张多个trait。但与其他类型不同，trait不能实例化.
3. 类似对象(Object), trait不能有*类参数*, 但与Object不同，可以有*类型参数*
4. trait的多重继承顺序/线性化(linearization)  
   - 多重继续顺序从右到左(从最低的子类到最高的基类),即最右的trait是所定义的类的直接父类  
   egg：  
   如果定义一个类:`class D extends A with B with C`  
   1)trait在父类后面。如果扩张了一个类和多个trait，需要先扩张这个类(extends classX)，再扩展trait(with traitX)  
   2)编译器最终实现为:`calss D extends C extends B extends A`  
   3)该顺序影响override，如多个trait有相同的字段或者方法，必须使用override关键字  
5. 用Trait实例化  
   1)在类定义中使用extends或者with关键字让类扩展trait。egg:`class D extends A with B with C`   
   2)在类实例化时为类增加trait。egg:
    ```scala
    class A
    trait B { self: A => }
    val a = new A with B
    ```
    使用trait实例化真正的意义在于可以为现有的类增加新的功能,或者配置。这个特性通常称为依赖注入(dependency injection)
    
# 自类型(self type)
    待补充笔记:《scala学习手册》P179
   **语法：定义子类型**
   `trait ... { <identifier>: <type> => ... }`  
   >自类型中使用标准标识符是"self",可以用任何其他标识符。
   >自类型是一个trait注解,向一个类增加到这个trait时，要求这个类必须有一个特定的类型或者子类型。
   >自类型的流行用法是用trait为需要输入参数的类增加功能.trait扩展有输入参数的类并不容易，因为trait本身不能有输入参数，不过trait可以用一个自类型的注解声明自己是这个父类的一个子类型，然后证据相应的功能。
   - 示例
   ```scala
   class TestSuite(suiteName: String) { def start(){} }
   
   //下面定义一个trait需要调用TestSuite的start方法,但是不能扩展TestSuite，因为它需要硬编码的输入参数，而trait是不能带参数。可以通过使用一个自类型，就可以认为这个trait是TestSuit的一个子类型，而不需要显示声明.
   trait RandomSeeded { self: TestSuite =>
       def randomStart(){
           util.Random.setSeed(System.currentTimeMillis())
           self.start()
       }
   }
   
   class IdSpec extends TestSuite("ID Test") with RandomSeeded{
     def testId(){ 
       println( util.Random.nextInt != 1 )
     }
     
     override def start() = testId()
     println("Starting ... ")
     
     randomStart()
     
   }
   ```
   >利用自类型，trait可以扩展一个类而不用指定其输入参数。也是一个为trait增加限制和/或需要的安全方法，可以确认他们只能在特定的上下文中使用.
      
