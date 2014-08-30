name := """gem-exec-test"""

scalaVersion := "2.10.4"

resolvers +=
  "rubygems-release" at "http://rubygems-proxy.torquebox.org/releases"

libraryDependencies ++= Seq(
  "rubygems" % "cowsay" % "0.1.0"
)
