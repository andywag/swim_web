import sbt.Project.projectToRef

import com.typesafe.sbt.packager.docker._

name := """swim-web"""

lazy val clients = Seq(client, client2, meets)
lazy val scalaV = "2.11.7"


lazy val core = (project in file("core")).settings(
  scalaVersion := scalaV,
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.9",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
    "joda-time" % "joda-time" % "2.9",
    "org.reactivemongo" %% "reactivemongo" % "0.11.7",
    "io.spray" %% "spray-can" % "1.3.3",
    "io.spray" %% "spray-client" % "1.3.3",
    "io.spray" %% "spray-httpx" % "1.3.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    //"org.slf4j" % "slf4j-log4j12" % "1.7.12",
    "org.slf4j" % "slf4j-api" % "1.7.13",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24",
    "com.lihaoyi" %% "upickle" % "0.3.6",
    "org.apache.pdfbox" % "pdfbox" % "1.8.10",
    "org.seleniumhq.selenium" % "selenium-java" % "2.48.2"
  )
).dependsOn(sharedJvm)

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    //"org.webjars" % "jquery" % "1.11.1",
    //"com.lihaoyi" %% "scalarx" % "0.2.8",
    //"org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
    "joda-time" % "joda-time" % "2.9",
    //"org.reactivemongo" %% "reactivemongo" % "0.11.7",
    //"io.spray" %% "spray-can" % "1.3.3",
    //"io.spray" %% "spray-client" % "1.3.3",
    //"io.spray" %% "spray-httpx" % "1.3.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "org.slf4j" % "slf4j-log4j12" % "1.7.12",
    //"org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24",
    //"org.webjars" % "datatables" % "1.10.9",
    //"org.webjars" % "d3js" % "3.5.6",
    //"org.webjars" % "nvd3" % "1.8.1",
    "com.lihaoyi" %% "upickle" % "0.3.6",
    //"com.lihaoyi" %% "autowire" % "0.2.4",
    //"org.apache.pdfbox" % "pdfbox" % "1.8.10",
    //"org.seleniumhq.selenium" % "selenium-java" % "2.48.2",

    //"com.github.japgolly.scalajs-react" %%% "core" % "0.10.1",
    specs2 % Test
  ),
  // Heroku specific
  herokuAppName in Compile := "your-heroku-app-name",
  herokuSkipSubProjects in Compile := false
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm, core)

lazy val clientShared = (project in file("clientShared")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.github.japgolly.scalajs-react" %%% "core" % "0.10.1" ,
    "com.lihaoyi" %%% "scalarx" % "0.2.8"  ,
    "com.lihaoyi" %%% "scalatags" % "0.5.3",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
    "com.lihaoyi" %%% "upickle" % "0.3.6",
    "org.singlespaced" %%% "scalajs-d3" % "0.1.1"
  ),

  jsDependencies += "org.webjars" % "bootstrap" % "3.3.6" / "bootstrap.js" minified "bootstrap.min.js",
  jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "jquery.js" minified "jquery.min.js",
  jsDependencies += "org.webjars" % "datatables" % "1.10.9" / "jquery.dataTables.js"  minified "jquery.dataTables.min.js" dependsOn "jquery.js",
  jsDependencies += "org.webjars" % "datatables" % "1.10.9" / "dataTables.bootstrap.js"  minified "dataTables.bootstrap.min.js" dependsOn "jquery.dataTables.js",
  jsDependencies += "org.webjars" % "d3js" % "3.5.6" / "d3.js" minified "d3.min.js",
  jsDependencies += "org.webjars" % "nvd3" % "1.8.1" / "nv.d3.js" minified "nv.d3.min.js" dependsOn "d3.js"

).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= Seq(
  )//,

).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs, clientShared)

lazy val client2 = (project in file("client2")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= Seq()

).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs, clientShared)

lazy val meets = (project in file("meets")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= Seq()
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs, clientShared)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.9",
      "com.lihaoyi" %% "upickle" % "0.3.6"
    )
  ).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator

javaOptions in Test +="-Dlogger.resource=logback-test.xml"

dockerCommands := Seq(
  Cmd("FROM","java:8"),
  Cmd("MAINTAINER","Name <email>"),
  Cmd("EXPOSE", "9000"),
  Cmd("ADD", "stage /"),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("RUN", "[\"chmod\", \"+x\", \"/opt/docker/bin/play-beanstalk\"]"),
  Cmd("RUN", "apt-get update && apt-get install -y supervisor"),
  Cmd("RUN", "[\"mkdir\", \"-p\", \"/var/log/supervisor\"]"),
  Cmd("ADD", "supervisord.conf /etc/supervisor/conf.d/supervisord.conf"),
  Cmd("CMD", "supervisord -c /etc/supervisor/conf.d/supervisord.conf")
)





