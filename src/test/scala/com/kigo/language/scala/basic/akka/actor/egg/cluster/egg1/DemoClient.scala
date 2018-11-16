package com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.backend.TransformationBackendApp
import com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.frontend.TransformationFrontendApp
import com.typesafe.config.ConfigFactory

import scala.io.StdIn
import scala.concurrent.duration._
/**
  * scalaVersion := "2.11.8"
  * val akkaVersion = "2.5.3"
  *
  * "com.typesafe.akka" %% "akka-actor"         % akkaVersion,
    "com.typesafe.akka" %% "akka-remote"        % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster"       % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-contrib"       % akkaVersion,
    "com.twitter" %% "chill-akka" % "0.8.4"
  *
  * Created by kigo on 18-3-25.
  */
object DemoClient {
  def main(args : Array[String]) {

    TransformationFrontendApp.main(Seq("2551").toArray)  //启动集群客户端
    TransformationBackendApp.main(Seq("8001").toArray)   //启动三个后台节点
    TransformationBackendApp.main(Seq("8002").toArray)
    TransformationBackendApp.main(Seq("8003").toArray)

    //val system = ActorSystem("OTHERSYSTEM") //不指定配置文件，默认加载application.conf
    //加载指定的配置文件
    val system = ActorSystem("OTHERSYSTEM",ConfigFactory.load("akka/application_clusterEgg1.conf"))

    val clientJobTransformationSendingActor =
      system.actorOf(Props[ClientJobTransformationSendingActor],
        name = "clientJobTransformationSendingActor")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {   //定时发送任务
      clientJobTransformationSendingActor ! Send(counter.incrementAndGet())
    }
    StdIn.readLine()
    system.terminate()
  }
}




