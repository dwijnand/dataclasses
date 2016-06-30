package dataclasses

import scala.meta._

import scala.{ collection => sc }, sc.{ immutable => sci }, sci.{ Seq => sciSeq }
import scala.runtime.ScalaRunTime

class data extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any) = meta {
    val q"""
      ..$mods0 class $tname[..$tparams] ..$ctorMods (...$paramss0) extends { ..$earlyStats } with ..$ctorcalls {
        $selfParam =>
        ..$stats
      }
    """ = defn

    val name    = Term.Name(tname.value)
    val ctorref = Ctor.Ref.Name(tname.value)

    val Any           = t"_root_.scala.Any"
    val AnyRef        = t"_root_.scala.AnyRef"
    val sciIndexedSeq = q"_root_.scala.collection.immutable.IndexedSeq"
    val Product       = t"_root_.scala.Product"
    val ProductImpl   = q"_root_.dataclasses.ProductImpl"
    val ScalaRunTime  = q"_root_.scala.runtime.ScalaRunTime"

    val mods = mods0 withMod Mod.Final()

    val params0 = paramss0.head

    val params  = params0 map (_ withMod Mod.ValParam())
    val paramss = params +: paramss0.tail

    val     aexprs = params0 map { case Term.Param(_, name @ Term.Name(_),             _, _) =>   arg"$name"                   }
    val copyParams = params0 map { case Term.Param(_, name @ Term.Name(_), Some(decltpe), _) => param"$name: $decltpe = $name" }
    val      withs = params0 map { case Term.Param(_, name @ Term.Name(_), Some(decltpe), _) =>
      val withName = Term.Name("with" + name.value.capitalize)
      q"def $withName($name: $decltpe = $name): $tname = copy($name = $name)"
    }

    val ctorNew = Term.New(Template(Nil, sciSeq(q"$ctorref(..$aexprs)"), Term.Param(Nil, Name.Anonymous(), None, None), None))

    val applyParams = params0 map (_ withoutMod Mod.ValParam())

    val classDefn = q"""
      ..$mods class $tname[..$tparams] ..$ctorMods (...$paramss) extends { ..$earlyStats } with ..$ctorcalls { $selfParam =>
        ..$stats;

        ..$withs;

        def copy(..$copyParams): $tname = $ctorNew

        override def toString = $ScalaRunTime._toString(asProduct)
        override def hashCode = $ScalaRunTime._hashCode(asProduct)
        override def equals(that: $Any) = (this eq that.asInstanceOf[$AnyRef]) || (that match {
          case that: $tname => $ScalaRunTime._equals(this.asProduct, that.asProduct)
          case _            => false
        })

        private def asProduct: $Product = $ProductImpl(${tname.value}, $sciIndexedSeq(..$aexprs))
      }
    """

    val objectDef = q"""
      object $name {
        def apply(..$applyParams): $tname = $ctorNew
      }
    """

    Term.Block(sciSeq(classDefn, objectDef))
  }
}

object `package` {
  implicit class ModWith_===(private val mod: Mod) extends AnyVal {
    def ===(thatMod: Mod) = ScalaRunTime._equals(mod, thatMod)
  }
  implicit class ModsWithMod(private val mods: sciSeq[Mod]) extends AnyVal {
    def    withMod(mod: Mod) = if (mods exists (mod === _)) mods else mods :+ mod
    def withoutMod(mod: Mod) = mods.filterNot(mod === _)
  }
  implicit class TermParamWithMod(private val param: Term.Param) extends AnyVal {
    def    withMod(mod: Mod) = if (param.mods exists (mod === _)) param else param.copy(mods = param.mods :+ mod)
    def withoutMod(mod: Mod) = param.copy(mods = param.mods.filterNot(mod === _))
  }
}
