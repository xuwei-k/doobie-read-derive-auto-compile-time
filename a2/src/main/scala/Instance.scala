package example

object Instance {
  given org.typelevel.doobie.util.Read[example.B] =
    org.typelevel.doobie.util.Read.derived[example.B]
}
