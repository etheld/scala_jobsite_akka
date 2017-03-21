name := "scala_jobsite"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5-M2"
libraryDependencies += "commons-validator" % "commons-validator" % "1.6"
libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"

//libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.1.0"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.1.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.1.0"
//libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.3.1"
//libraryDependencies += "org.apache.spark" %% "spark-streaming-flume" % "1.3.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.1.0"

