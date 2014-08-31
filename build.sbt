import bintray.Keys._

sbtPlugin := true

name := "sbt-rubygems"

organization := "org.jruby"

homepage := Some(url("https://github.com/jkutner/sbt-rubygems"))

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

// Configuration for generating json files for publishing at http://ls.implicit.ly
seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt", "ruby", "jruby", "rubygems", "gems")

(description in LsKeys.lsync) := "an sbt plugin for adding Rubygems to your project as dependencies."

(LsKeys.ghUser in LsKeys.lsync) := Some("jkutner")

(LsKeys.ghRepo in LsKeys.lsync) := Some("sbt-rubygems")

(LsKeys.ghBranch in LsKeys.lsync) := Some("master")