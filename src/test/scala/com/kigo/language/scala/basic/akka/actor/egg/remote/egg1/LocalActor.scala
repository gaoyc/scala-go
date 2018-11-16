package com.kigo.language.scala.basic.akka.actor.egg.remote.egg1

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by kigo on 18-3-25.
  */
case object Init
case object SendNoReturn
case object SendHasReturn
case object SendSerialization

case class JoinEvt(
                    id: Long,
                    name: String
                  )

class LocalActor extends Actor {

  val path =
//    ConfigFactory.defaultApplication().getString("remote.actor.name.test")
    ActorPath.formatted("akka.tcp://RemoteDemoSystem@127.0.0.1:4444/user/RemoteActor")

  implicit val timeout = Timeout(30.seconds)

  val remoteActor = context.actorSelection(path)

  def receive: Receive = {
    case Init => "init local actor"
    case SendNoReturn => remoteActor ! "hello remote actor"
    case SendHasReturn =>
      for {
        r <- remoteActor.ask("hello remote actor")
      } yield println(r)

    case SendSerialization =>
      for {
        r <- remoteActor.ask(JoinEvt(1L,"godpan"))
      } yield println(r)
  }

}


