package dataclassestest

import dataclassesexamples._
import org.specs2._
import org.specs2.execute._
import org.specs2.execute.Typecheck._
import org.specs2.matcher.TypecheckMatchers._

object DataSpec extends Specification { def is = s2"""
  The spec for @data

  @data class Bippy1(foo: Int) // 1-arity
    can be constructed with "new"   ${new Bippy1(1) must beAnInstanceOf[Bippy1]}
    has the correct toString        ${new Bippy1(1).toString ==== "Bippy1(1)"}
    two instances have the same ##  ${new Bippy1(1).## ==== new Bippy1(1).##}
    two instances are equal         ${new Bippy1(1) ==== new Bippy1(1)}
    can be constructed with "apply" ${Bippy1(1) ==== new Bippy1(1)}
    exposes its underlying type     ${new Bippy1(1).foo ==== 1}
    has a copy method               ${new Bippy1(1).copy(foo = 2).foo ==== 2}
    has a withFoo method            ${new Bippy1(1).withFoo(2).foo ==== 2}
    doesn't have a unapply method   $bippy1NoUnapply
      because of https://issues.scala-lang.org/browse/SI-9836

  can handle a constructor modifier ${typecheck("@dataclasses.data class PrivateBippy1 private (foo: Int)")}
  can handle a ctor param modifier  ${typecheck("@dataclasses.data class ValBippy1(val foo: Int)")}

  @data class LongBippy1(foo: Long) // not Int
    can be constructed with "new"   ${new LongBippy1(1L) must beAnInstanceOf[LongBippy1]}
    can be constructed with "apply" ${LongBippy1(1L) ==== new LongBippy1(1L)}
    has a copy method               ${new LongBippy1(1L).copy(foo = 2L).foo ==== 2L}
    has a withFoo method            ${new LongBippy1(1L).withFoo(2L).foo ==== 2L}

  @data class BarBippy1(bar: Int) // not foo
    can be constructed with "new"   ${new BarBippy1(1) must beAnInstanceOf[BarBippy1]}
    can be constructed with "apply" ${BarBippy1(1) ==== new BarBippy1(1)}
    has a copy method               ${new BarBippy1(1).copy(bar = 2).bar ==== 2}
    has a withBar method            ${new BarBippy1(1).withBar(2).bar ==== 2}

  @data class Bippy0() // 0-arity
    can be constructed with "new"   ${new Bippy0() must beAnInstanceOf[Bippy0]}
    has the correct toString        ${new Bippy0().toString ==== "Bippy0()"}
    two instances have the same ##  ${new Bippy0().## === new Bippy0().##}
    two instances are equal         ${new Bippy0 ==== new Bippy0}
    can be constructed with "apply" ${Bippy0() ==== new Bippy0}
    doesn't have a copy method      $bippy0NoCopy
    doesn't have a unapply method   $bippy0NoUnapply

  @data class Bippy2(foo: Int, bar: String) // 2-arity
    can be constructed with "new"     ${new Bippy2(1, "a") must beAnInstanceOf[Bippy2]}
    has the correct toString          ${new Bippy2(1, "a").toString ==== "Bippy2(1, a)"}
    two instances have the same ##    ${new Bippy2(1, "a").## ==== new Bippy2(1, "a").##}
    two instances are equal           ${new Bippy2(1, "a") ==== new Bippy2(1, "a")}
    can be constructed with "apply"   ${Bippy2(1, "a") ==== new Bippy2(1, "a")}
    exposes its underlying type       ${new Bippy2(1, "a").foo ==== 1}
    exposes its underlying type       ${new Bippy2(1, "a").bar ==== "a"}
    has a copy method for foo         ${new Bippy2(1, "a").copy(foo = 2).foo ==== 2}
    has a copy method for bar         ${new Bippy2(1, "a").copy(bar = "b").bar ==== "b"}
    has a copy method for foo and bar ${new Bippy2(1, "a").copy(foo = 2, bar = "b") ==== new Bippy2(2, "b")}
    has a withFoo method              ${new Bippy2(1, "a").withFoo(2).foo ==== 2}
    has a withBar method              ${new Bippy2(1, "a").withBar("b").bar ==== "b"}
    correct withFoo.withBar           ${new Bippy2(1, "a").withFoo(2).withBar("b") ==== new Bippy2(2, "b")}
    correct withBar.withFoo           ${new Bippy2(1, "a").withBar("b").withFoo(2) ==== new Bippy2(2, "b")}
    has an unapply                    ${Bippy2(1, "a") must beLike { case Bippy2(foo, bar) => foo ==== 1 and bar ==== "a" }}
"""

  private def bippy0NoCopy = (typecheck("Bippy0().copy()")
    must failWith("value copy is not a member of .*Bippy0") pendingUntilFixed "TODO")

  private def bippy0NoUnapply = (typecheck("Bippy0() match { case Bippy0() => 1 }")
    must failWith("object Bippy0 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def bippy1NoUnapply = (typecheck("Bippy1(1) match { case Bippy1(_) => 1 }")
    must failWith("object Bippy1 is not a case class, nor does it have an unapply/unapplySeq member"))
}
