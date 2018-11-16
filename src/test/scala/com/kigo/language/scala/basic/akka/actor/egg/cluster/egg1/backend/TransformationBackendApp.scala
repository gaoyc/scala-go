package com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.backend

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.language.postfixOps
/**
  * Created by kigo on 18-3-25.
  */


object TransformationBackendApp {
  def main(args: Array[String]): Unit = {
    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "2553" else args(0)
    //withFallback，使用指定配置覆盖配置文件中的对应配置项
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load("akka/application_clusterBackend.conf"))

    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[TransformationBackend], name = "backend")
  }
}