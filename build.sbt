import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt.Tests

lazy val commons = (project in file("modules/common"))
  .disablePlugins(GatlingPlugin)
  .settings(commonSettings: _*)
  .settings(scalariformSettings)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(libraryDependencies ++= commonDependenciesInTest)

lazy val stress = (project in file("modules/stress"))
  .enablePlugins(GatlingPlugin)
  .settings(commonSettings: _*)
  .settings(scalariformSettings)
  .settings(libraryDependencies ++= Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7",
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % "test",
    "io.gatling" % "gatling-test-framework" % "2.1.7" % "test"
  ))
  .dependsOn(commons)


lazy val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.7",
  ivyScala := ivyScala.value map {
    _.copy(overrideScalaVersion = true)
  },
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused-import",
    "-Ywarn-unused"
  ),
  javaOptions in Test ++= Seq(
  ),
  testOptions in Test += Tests.Setup { _ =>
    System.setProperty("AWS_ACCESS_KEY", "xxxxx")
    System.setProperty("AWS_SECRET_ACCESS_KEY", "yyyyy")
    System.setProperty("TD_API_KEY", "zzzzz")
  }
)


lazy val commonDependencies = Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.treasuredata" % "td-jdbc" % "0.5.3",
  "com.amazonaws" % "aws-java-sdk" % "1.10.50",
  "com.zaxxer" % "HikariCP" % "2.4.5",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.treasuredata" % "td-client" % "0.5.10",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
  "com.github.nscala-time" %% "nscala-time" % "2.8.0",
  "com.github.seratch" %% "awscala" % "0.5.+",
  "com.google.inject" % "guice" % "4.0",
  "org.json4s" %% "json4s-jackson" % "3.2.10",
  "joda-time" % "joda-time" % "2.9.1",
  "mysql" % "mysql-connector-java" % "5.1.32",
  "org.apache.commons" % "commons-dbcp2" % "2.1.1",
  "org.joda" % "joda-convert" % "1.7",
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "org.scalikejdbc" %% "scalikejdbc" % "2.3.5",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.3.5",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.3.5"
)

lazy val commonDependenciesInTest = Seq(
  "org.specs2" %% "specs2-core" % "3.7.2-scalaz-7.2.0" % "test",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.3.5" % "test",
  "org.specs2" % "specs2-mock_2.11" % "3.7.2"
)

lazy val scalariformSettings = SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
)
