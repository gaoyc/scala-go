package com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1
import akka.actor.Actor
import akka.actor.ActorPath
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.pattern.Patterns
import akka.util.Timeout
import com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.backend.{TransformationJob, TransformationResult}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
/**
  * Created by kigo on 18-3-25.
  */
class ClientJobTransformationSendingActor extends Actor {

  val initialContacts = Set(
    ActorPath.fromString("akka.tcp://ClusterSystem@127.0.0.1:2551/system/receptionist"))
  val settings = ClusterClientSettings(context.system)
    .withInitialContacts(initialContacts)

  val c = context.system.actorOf(ClusterClient.props(settings), "demo-client")


  def receive = {
    case TransformationResult(result) => {
      println(s"Client response and the result is ${result}")
    }
    case Send(counter) => {
      val job = TransformationJob("hello-" + counter)
      implicit val timeout = Timeout(5 seconds)
      val result = Patterns.ask(c,ClusterClient.Send("/user/frontend", job, localAffinity = true), timeout)

      result.onComplete {
        case Success(transformationResult) => {
          self ! transformationResult
        }
        case Failure(t) => println("An error has occured: " + t.getMessage)
      }
    }
  }
}
