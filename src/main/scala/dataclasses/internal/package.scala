package dataclasses

import scala.meta._

import scala.{ collection => sc }, sc.{ immutable => sci }, sci.{ Seq => sciSeq }
import scala.runtime.ScalaRunTime

package object internal {
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
