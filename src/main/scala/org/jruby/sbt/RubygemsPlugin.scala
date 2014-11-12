package org.jruby.sbt

import sbt.Keys._
import sbt._
import sbt.compiler.CompileFailed
import sbt.complete._
import complete.DefaultParsers._

import org.jruby.RubyInstanceConfig

object RubygemsPlugin extends AutoPlugin {

  object autoImport {

    val gemExec = inputKey[Unit]("Run a gem command.")

    lazy val baseRubygemsSettings: Seq[Def.Setting[_]] = Seq(
      gemExec := {
        val args: Seq[String] = spaceDelimited("<arg>").parsed
        gemExecAction(args, target.value / "rubygems", (update in Compile).value.allFiles, streams.value.log)
      }
    )
  }

  import org.jruby.sbt.RubygemsPlugin.autoImport._

  override def requires = sbt.plugins.JvmPlugin

  override def trigger = allRequirements

  override val projectSettings =
    inConfig(Compile)(baseRubygemsSettings)

  def jruby(rubyGemsHome: File): org.jruby.Main = {
    val ruby = new RubyInstanceConfig()
    val env = ruby.getEnvironment.asInstanceOf[java.util.Map[String,String]]
    val newEnv = new java.util.HashMap[String, String]
    newEnv.putAll(env)
    newEnv.put("GEM_HOME", rubyGemsHome.getAbsolutePath)
    newEnv.put("GEM_PATH", rubyGemsHome.getAbsolutePath)
    ruby.setEnvironment(newEnv)
    new org.jruby.Main(ruby)
  }

  def gemExecAction(args: Seq[String], rubyGemsHome: File, allDepenencyFiles: Seq[File], log: Logger): Unit = {
    IO.createDirectory(rubyGemsHome)
    val oldContextClassLoader = Thread.currentThread.getContextClassLoader
    Thread.currentThread.setContextClassLoader(this.getClass.getClassLoader)
      allDepenencyFiles.foreach { f =>
        if (f.getName.endsWith(".gem")) {
          if ((rubyGemsHome / "cache" / f.getName).exists) {
            log.info("Using target/rubygems/cache/" + f.getName)
          } else if ((rubyGemsHome / "cache" / f.getName.replaceAll(".gem", "-java.gem")).exists) {
              log.info("Using target/rubygems/cache/" + f.getName.replaceAll(".gem", "-java.gem"))
          } else {
            log.debug("Installing " + f.getName)
            jruby(rubyGemsHome).run(
              List("-S", "gem", "install", f.getAbsolutePath, "-f", "-l", "-i",
                rubyGemsHome.getAbsolutePath).toArray[String])
          }
        }
      }
      val rubyGemsBin = rubyGemsHome / "bin"
      val status = jruby(rubyGemsHome).run(
        (List("-S", rubyGemsBin.getAbsolutePath.replaceAllLiterally("\\", "/").replaceAllLiterally("C:","/C:") + "/" + args(0)) ++ args.slice(1, args.size)).toArray[String])
      if (status.getStatus != 0) {
        throw new CompileFailed(Array(), "Gem command failed!", Array())
      }
    Thread.currentThread.setContextClassLoader(oldContextClassLoader)
  }
}
