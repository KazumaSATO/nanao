lazy val root = (project in file(".")).
  settings(
    name := "nanao",

    organization := "com.ranceworks",

    version := "1.0.1",

    scalaVersion := "2.11.7",

    resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika",

    libraryDependencies += "com.atilika.kuromoji" % "kuromoji-ipadic" % "0.9.0",

    useGpg := true,

    publishMavenStyle := true,

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    sonatypeProfileName := "com.ranceworks",

    pomExtra := (
      <url>https://github.com/satokazuma/nanao</url>
        <licenses>
          <license>
            <name>The MIT License</name>
            <url>https://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>https://github.com/satokazuma/nanao</url>
          <connection>https://github.com/satokazuma/nanao.git</connection>
        </scm>
        <developers>
          <developer>
            <id>satokazuma</id>
            <name>Sato Kazuma</name>
            <url>http://ranceworks.com</url>
          </developer>
        </developers>
    )
  )
