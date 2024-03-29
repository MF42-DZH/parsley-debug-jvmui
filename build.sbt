Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  Seq(
    scalaVersion     := "2.13.10",
    version          := "0.1.0-SNAPSHOT",
    organization     := "com.github.mf42-dzh",
    organizationName := "github",
    javacOptions    ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions   ++= Seq("-deprecation", "-unchecked", "-feature")
  )
)

// Warning: currently uses the snapshot locally listed on the developer's computer.
lazy val root = (project in file("."))
  .settings(
    name                := "parsley-debug-jvmui",
    resolvers          ++= Opts.resolver.sonatypeOssSnapshots,
    // ScalaFX v19.0.0-R30 is the latest supported by Java 8.
    libraryDependencies += "org.scalafx" %% "scalafx" % "19.0.0-R30",
    libraryDependencies += "com.github.j-mie6" %% "parsley" % "4.4-de8306a-SNAPSHOT",
    libraryDependencies += "com.github.j-mie6" %% "parsley-debug" % "4.4-de8306a-SNAPSHOT",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
  )
