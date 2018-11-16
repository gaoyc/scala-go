package com.kigo.language.scala.basic.akka.actor.egg.officer

import akka.actor.{ActorSystem, Actor, ActorRef, Props, PoisonPill}
import language.postfixOps
import scala.concurrent.duration._

/**
  *
  * 参考自官方文档: https://doc.akka.io/docs/akka/snapshot/actors.html#defining-an-actor-class
  * Created by kigo on 18-3-24.
  */

case object Ping
case object Pong

class Pinger extends Actor {
  var countDown = 100

  def receive = {
    case Pong ⇒
      println(s"${self.path} received pong, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping ⇒
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}

object PingPongMain extends App {


  val system = ActorSystem("pingpong")

  val pinger = system.actorOf(Props[Pinger], "pinger")

  val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")

  import system.dispatcher

  system.scheduler.scheduleOnce(500 millis) {
    ponger ! Ping
  }

  // $FiddleDependency org.akka-js %%% akkajsactor % 1.2.5.1
}

