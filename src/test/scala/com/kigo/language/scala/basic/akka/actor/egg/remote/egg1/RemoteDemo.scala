package com.kigo.language.scala.basic.akka.actor.egg.remote.egg1

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * 说明: 本样例整理修改自: 有道笔记 =>《Akka系列（九）：Akka分布式之Akka Remote》
  * Created by kigo on 18-3-25.
  */
object RemoteDemo extends App  {

  //如果并没有注明任何的配置，它将在classpath中寻找 application.conf、 application.json和application.properties，并自动的加载他们
  //val system = ActorSystem("RemoteDemoSystem")
  val system = ActorSystem("RemoteDemoSystem", ConfigFactory.load("application_RemoteActor.conf"))
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
  remoteActor ! "The RemoteActor is alive"
}
