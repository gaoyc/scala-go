package com.kigo.language.scala.basic.akka.actor.egg.cluster.egg1.backend

/**
  * Created by kigo on 18-3-25.
  */
final case class TransformationJob(text: String) // 任务内容
final case class TransformationResult(text: String) // 执行任务结果
final case class JobFailed(reason: String, job: TransformationJob) //任务失败相应原因
case object BackendRegistration // 后台具体执行任务节点注册事件
