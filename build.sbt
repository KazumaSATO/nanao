lazy val root = (project in file(".")).
  settings(
    name := "nanao",

    organization := "com.ranceworks",

    version := "1.0-SNAPSHOT",

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
      <url>https://github.com/KazumaSATO/nanao</url>
        <licenses>
          <license>
            <name>The MIT License</name>
            <url>https://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>https://github.com/KazumaSATO/nanao</url>
          <connection>scm:git:https://github.com/KazumaSATO/nanao.git</connection>
        </scm>
        <developers>
          <developer>
            <id>kzstm</id>
            <name>Kazuma SATO</name>
            <url>http://ranceworks.com</url>
          </developer>
        </developers>
    )
  )
