package dataclasses

import scala.meta._

import scala.{ collection => sc }, sc.{ immutable => sci }, sci.{ Seq => sciSeq }
import scala.runtime.ScalaRunTime

//@data class Bippy1(foo: Int)

//object Bippy {
//  def apply(foo: Int): Bippy = new Bippy(foo)
//}

class data extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta {
    val q"..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends $template" = defn
    val template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$stats }" = template

    val tnameString  = tname.value
    val ScalaRunTime = q"_root_.scala.runtime.ScalaRunTime"
    val Any          = t"_root_.scala.Any"
    val AnyRef       = t"_root_.scala.AnyRef"
    val Product      = t"_root_.scala.Product"
    val ProductImpl  = q"_root_.dataclasses.ProductImpl"
    val IndexedSeq   = q"_root_.scala.collection.immutable.IndexedSeq"

    val newMods = mods withMod Mod.Final()

    val params1: sciSeq[Term.Param] = paramss.head
    val newParamss = (params1 map (_ withMod Mod.ValParam())) +: paramss.tail

    val withs = params1 map { p =>
      val name @ Term.Name(_) = p.name
      val Some(decltpe) = p.decltpe
      val withName = Term.Name("with" + name.value.capitalize)
      q"def $withName($name: $decltpe = $name): $tname = copy($name = $name)"
    }

    val copyParams = params1 map { p =>
      val name @ Term.Name(_) = p.name
      val Some(decltpe) = p.decltpe
      param"$name: $decltpe = $name"
    }

    val ctorRefName = Ctor.Ref.Name(tnameString)
    val ctorApply = q"$ctorRefName(foo)"
    val ctorNew = Term.New(
      Template(
        Nil,
        sciSeq(ctorApply),
        Term.Param(Nil, Name.Anonymous(), None, None),
        None
      )
    )

//  val copy = q"def copy(..$copyParams): $tname = new $tname(foo)"
    val copy = q"def copy(..$copyParams): $tname = $ctorNew"

    val toString = q"override def toString = $ScalaRunTime._toString(asProduct)"
    val hashCode = q"override def hashCode = $ScalaRunTime._hashCode(asProduct)"
    val equals = q"""override def equals(that: $Any) = (this eq that.asInstanceOf[$AnyRef]) || (that match {
      case that: $tname => $ScalaRunTime._equals(this.asProduct, that.asProduct)
      case _            => false
      })"""

    val asProduct = q"private def asProduct: $Product = $ProductImpl($tnameString, $IndexedSeq(foo))"

    val applyParams = params1 map { p =>
      val name @ Term.Name(_) = p.name
      val Some(decltpe) = p.decltpe
      param"$name: $decltpe"
    }
    val apply = q"def apply(..$applyParams): $tname = $ctorNew"

    val newStats = stats ++ withs :+ copy :+ toString :+ hashCode :+ equals :+ asProduct

    val newTemplate = template"{ ..$earlydefns } with ..$ctorcalls { $param => ..$newStats }"
    val classDefn = q"..$newMods class $tname[..$tparams] ..$ctorMods (...$newParamss) extends $newTemplate"

    val objectDef = q"object ${Term.Name(tnameString)} { $apply }"

    Term.Block(sciSeq(classDefn, objectDef))
  }
}

object `package` {
  implicit class ModWith_===(private val m: Mod) extends AnyVal {
    def ===(mod: Mod) = ScalaRunTime._equals(mod, m)
  }
  implicit class ModsWithMod(private val mods: sciSeq[Mod]) extends AnyVal {
    def withMod(mod: Mod) = if (mods exists (mod === _)) mods else mods :+ mod
  }
  implicit class TermParamWithMod(private val p: Term.Param) extends AnyVal {
    def withMod(mod: Mod) = if (p.mods exists (mod === _)) p else p.copy(mods = p.mods :+ mod)
  }
}
