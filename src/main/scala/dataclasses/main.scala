package dataclasses

import scala.meta._

class main extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta {
    val q"..$mods object $name extends $template" = defn
    val template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$stats }" = template
    val main = q"""def main(args: Array[String]): Unit = { ..$stats }"""
    val newTemplate = template"{ ..$earlydefns } with ..$ctorcalls { $param => $main }"
    q"..$mods object $name extends $newTemplate"
  }
}
