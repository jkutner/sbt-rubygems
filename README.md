Rubygems sbt Plugin
==================

This is an sbt plugin for adding [Rubygems](http://rubygems.org/) to your project as dependencies.

## Using

Add this to your `project/plugins.sbt`

```
resolvers += Resolver.url("bintray-sbt-plugin-releases", url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.jruby" % "sbt-rubygems" % "1.0)
```

Then add some Rubygems to your `build.sbt` like so:

```
resolvers +=
  "rubygems-release" at "http://rubygems-proxy.torquebox.org/releases"

libraryDependencies ++= Seq(
  "rubygems" % "cowsay" % "0.1.0"
)
```

And now you can run commands like this:

```sh-session
$ sbt
...
> gemExec cowsay "Hello World!"
Successfully installed cowsay-0.1.0
1 gem installed
 ______________
| Hello World! |
 --------------
 ...
 [success] Total time: 28 s, completed Aug 30, 2014 8:46:58 AM
```
