package dataclasses

import scala.meta._

//@data class Bippy1(foo: Int)
class data extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta {
    // TODO: Add back the constructor mod
    val q"..$mods class $tname[..$tparams](...$paramss) extends $template" = defn
    val template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$stats }" = template
    val toString = q"""override def toString: _root_.java.lang.String = "hi""""
    val newStats = stats :+ toString
    val newTemplate = template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$newStats }"
    q"..$mods class $tname[..$tparams](...$paramss) extends $newTemplate"
  }
}
