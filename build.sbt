val base = Def.settings(
  scalaVersion := "3.9.0",
)

autoScalaLibrary := false

def gen(header: String) = (
  Compile / sourceGenerators += task {
    val dir = (Compile / sourceManaged).value
    (1 to classCount).map { n =>
      val f = dir / s"A${n}.scala"
      IO.write(
        f,
        s"""|package example
            |
            |${header}
            |
            |class A${n} {
            |
            |${(1 to methodCount).map {i => s"""  def x${i} = implicitly[org.typelevel.doobie.util.Read[example.B]]"""}.mkString("\n") }
            |
            |}
            |""".stripMargin
      )
      f
    }
  }
)

val common = Def.settings(
  base,
  libraryDependencies += "org.typelevel" %% "doobie-core" % "1.0.0-RC13",
)

def methodCount = 200
def classCount = 100

val x = project
  .settings(
    base,
  )

val a1 = project
  .settings(
    common,
    gen("import org.typelevel.doobie.generic.auto._"),
  )
  .dependsOn(x)

val a2 = project
  .settings(
    common,
    gen("import example.Instance.given"),
  )
  .dependsOn(x)
