package dataclasses

class data extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta { internal.DataMacros.impl(defn) }
}
