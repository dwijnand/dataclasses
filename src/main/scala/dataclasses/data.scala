package dataclasses

import scala.meta._

import scala.collection.immutable.{ Seq => sciSeq }

//@data class Bippy1(foo: Int)

//final class Bippy(val foo: Int) {
//  def withFoo(foo: Int): Bippy = copy(foo = foo)
//
//  def copy(foo: Int = foo): Bippy = Bippy(foo)
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

    val ScalaRunTime = q"_root_.scala.runtime.ScalaRunTime"
    val AnyRef       = t"_root_.scala.AnyRef"
    val ProductImpl  = q"_root_.dataclasses.ProductImpl"
    val IndexedSeq   = q"_root_.scala.collection.immutable.IndexedSeq"

    val params1: sciSeq[Term.Param] = paramss.head
    val newParams1 = params1.map(param =>
      if (param.mods exists { case Mod.ValParam() => true; case _ => false }) param
      else param.copy(mods = param.mods :+ Mod.ValParam())
    )
    val newParamss = newParams1 +: paramss.tail

//  val copy = q"def copy(foo: Int = foo): $tname = new $tname(foo)"

    val fooParam = param"foo: _root_.scala.Int = foo"

    val ctorRefName = Ctor.Ref.Name(tname.value)
    val ctorApply = q"$ctorRefName(foo)"
    val copyBody = Term.New(
      Template(
        Nil,
        sciSeq(ctorApply),
        Term.Param(Nil, Name.Anonymous(), None, None),
        None
      )
    )

    val copy = Defn.Def(
      Nil,
      Term.Name("copy"),
      Nil,
      sciSeq(sciSeq(fooParam)),
      Some(tname),
      copyBody
    )

    val toString = q"override def toString = $ScalaRunTime._toString(asProduct)"
    val hashCode = q"override def hashCode = $ScalaRunTime._hashCode(asProduct)"
    val equals = q"""override def equals(that: _root_.scala.Any) = (this eq that.asInstanceOf[$AnyRef]) || (that match {
      case that: $tname => $ScalaRunTime._equals(this.asProduct, that.asProduct)
      case _            => false
      })"""

    val asProduct = q"private def asProduct: _root_.scala.Product = $ProductImpl(${tname.value}, $IndexedSeq(foo))"

    val newStats = stats :+ copy :+ toString :+ hashCode :+ equals :+ asProduct

    val newTemplate = template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$newStats }"
    q"..$mods class $tname[..$tparams](...$newParamss) extends $newTemplate"
  }
}
