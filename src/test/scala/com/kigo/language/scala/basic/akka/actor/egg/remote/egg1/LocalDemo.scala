package com.kigo.language.scala.basic.akka.actor.egg.remote.egg1

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  *
  * Created by kigo on 18-3-25.
  */
object LocalDemo extends App {
  // //而该并没有注明任何的配置，它将在classpath中寻找 application.conf、 application.json和application.properties，并自动的加载他们
  //implicit val system = ActorSystem("LocalDemoSystem")
  val config = ConfigFactory.load("application_LocalActor.conf")
  implicit val system = ActorSystem("LocalDemoSystem", config)
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")

  localActor ! Init
  //localActor ! SendNoReturn
  localActor ! SendHasReturn
}
