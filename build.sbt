name := "anormdb-demo2"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases"
// https://mvnrepository.com/artifact/org.scalikejdbc/scalikejdbc-config
libraryDependencies += "org.scalikejdbc" % "scalikejdbc-config_2.11" % "2.2.5"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.5.1",
  "com.h2database" % "h2" % "1.4.191"
)
// https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25" % "test"

        