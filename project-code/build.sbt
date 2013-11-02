name := "play2-sentry-plugin"

version := "0.1.0"

organization := "ru.purecode"

libraryDependencies ++= Seq(
  "net.kencochrane.raven" % "raven" % "4.1.1"
)

play.Project.playJavaSettings
