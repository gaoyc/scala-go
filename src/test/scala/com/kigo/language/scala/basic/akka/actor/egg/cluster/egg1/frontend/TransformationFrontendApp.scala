package com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.frontend
import language.postfixOps
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import akka.cluster.client.ClusterClientReceptionist
/**
  * Created by kigo on 18-3-25.
  */
object TransformationFrontendApp {

  def main(args: Array[String]): Unit = {

    val cofing = ConfigFactory.load("akka/application_clusterFrontend.conf")
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(cofing)

    val system = ActorSystem("ClusterSystem", config)
    val frontend = system.actorOf(Props[TransformationFrontend], name = "frontend")
    ClusterClientReceptionist(system).registerService(frontend)
  }

}
