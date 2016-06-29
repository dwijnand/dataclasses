package dataclasses

import scala.meta._

//@data class Bippy1(foo: Int)

//final class Bippy(val foo: Int) {
//  def withFoo(foo: Int): Bippy = copy(foo = foo)
//
//  def copy(foo: Int = foo): Bippy = Bippy(foo)
//
//  override def toString = scala.runtime.ScalaRunTime._toString(this.asProduct)
//  override def hashCode = scala.runtime.ScalaRunTime._hashCode(this.asProduct)
//  override def equals(that: Any) = (this eq that.asInstanceOf[AnyRef]) || (that match {
//    case that: Bippy => scala.runtime.ScalaRunTime._equals(this.asProduct, that.asProduct)
//    case _           => false
//  })
//
//  private def asProduct: Product = ProductImpl("Bippy", IndexedSeq(foo))
//}
//
//object Bippy {
//  def apply(foo: Int): Bippy = new Bippy(foo)
//}

class data extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta {
    // TODO: Add back the constructor mod
    val q"..$mods class $tname[..$tparams](...$paramss) extends $template" = defn
    val template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$stats }" = template
    val ProductImpl = q"_root_.dataclasses.ProductImpl"
    val IndexedSeq = q"_root_.scala.collection.immutable.IndexedSeq"
    val asProduct = q"private def asProduct: _root_.scala.Product = $ProductImpl(${tname.value}, $IndexedSeq(foo))"
    val ScalaRunTime = q"_root_.scala.runtime.ScalaRunTime"
    val toString = q"override def toString = $ScalaRunTime._toString(asProduct)"
    val newStats = stats :+ toString :+ asProduct
    val newTemplate = template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$newStats }"
    q"..$mods class $tname[..$tparams](...$paramss) extends $newTemplate"
  }
}
