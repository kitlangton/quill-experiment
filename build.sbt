ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val core = (project in file("core"))
  .settings(
    name := "quill-experiment",
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio"            % "2.0.0-RC2",
      "dev.zio"       %% "zio-streams"    % "2.0.0-RC2",
      "dev.zio"       %% "zio-test"       % "2.0.0-RC2" % Test,
      "dev.zio"       %% "zio-test-sbt"   % "2.0.0-RC2" % Test,
      "io.getquill"   %% "quill-jdbc-zio" % "3.17.0-RC2",
      "org.postgresql" % "postgresql"     % "42.3.3"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    scalacOptions += "-language:experimental.macros",
    libraryDependencies ++=
      Seq(
        "org.scala-lang" % "scala-reflect"  % scalaVersion.value % "provided",
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
      )
  )
  .dependsOn(macros)

lazy val macros = (project in file("macros"))
  .settings(
    name := "quill-experiment-macros",
    libraryDependencies ++= Seq(
      "dev.zio"     %% "zio"            % "2.0.0-RC2",
      "dev.zio"     %% "zio-streams"    % "2.0.0-RC2",
      "dev.zio"     %% "zio-test"       % "2.0.0-RC2" % Test,
      "dev.zio"     %% "zio-test-sbt"   % "2.0.0-RC2" % Test,
      "io.getquill" %% "quill-jdbc-zio" % "3.17.0-RC2"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    scalacOptions += "-language:experimental.macros",
    libraryDependencies ++=
      Seq(
        "org.scala-lang" % "scala-reflect"  % scalaVersion.value % "provided",
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
      )
  )
