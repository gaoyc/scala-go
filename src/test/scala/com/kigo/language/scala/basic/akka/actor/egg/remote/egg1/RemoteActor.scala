package com.kigo.language.scala.basic.akka.actor.egg.remote.egg1

import akka.actor.Actor

/**
  * Created by kigo on 18-3-25.
  */
class RemoteActor extends Actor {
  def receive = {
    case msg: String =>
      println(s"RemoteActor received message '$msg'")
      sender ! "Hello from the RemoteActor"
  }

}



