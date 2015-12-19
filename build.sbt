lazy val root = (project in file(".")).
  settings(
    name := "nanao",
    version := "1.0",
    scalaVersion := "2.11.7",
    resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika",
    libraryDependencies += "com.atilika.kuromoji" % "kuromoji-ipadic" % "0.9.0"
  )
