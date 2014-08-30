import bintray.Keys._

sbtPlugin := true

name := "sbt-rubygems"

organization := "org.jruby"

scalaVersion in Global := "2.10.4"

version := "1.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.jruby" % "jruby-complete" % "1.7.13",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.50",
  "org.bouncycastle" % "bcpg-jdk15on" % "1.50"
)

scriptedSettings

scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }

publishMavenStyle := false

bintrayPublishSettings

repository in bintray := "sbt-plugins"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in bintray := None
