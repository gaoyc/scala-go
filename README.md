##1.创建初始工程要点记录
(1)idea操作
  File => new => Project => Maven  
  勾选[Create from archetype]  
  模板选择: org.scala-tools.archetypes:scala-archetype-simple  
(2) 创建完成后，修改pom.xml中的scala-library, junit版本; 必须时修改注释其中repositories（在其不能联网使用情况下,直接使用maven默认仓库）