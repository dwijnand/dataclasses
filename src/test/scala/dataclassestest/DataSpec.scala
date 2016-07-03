package dataclassestest

import dataclassesexamples._
import org.specs2._
import org.specs2.execute._
import org.specs2.execute.Typecheck._
import org.specs2.main.NoArgProperties
import org.specs2.matcher.TypecheckMatchers._

object DataSpec extends Specification with NoArgProperties { def is = s2"""
  The spec for @data

  # 1-arity class
  @data class Bippy1(foo: Int)
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

  can handle a constructor modifier  ${typecheck("@dataclasses.data class PrivateBippy1 private (foo: Int)")}
  can handle a ctor param modifier   ${typecheck("@dataclasses.data class ValBippy1(val foo: Int)")}
  can't handle a pre-existing object $compBippy

  # Not Int
  @data class LongBippy1(foo: Long)
    can be constructed with "new"   ${new LongBippy1(1L) must beAnInstanceOf[LongBippy1]}
    can be constructed with "apply" ${LongBippy1(1L) ==== new LongBippy1(1L)}
    has a copy method               ${new LongBippy1(1L).copy(foo = 2L).foo ==== 2L}
    has a withFoo method            ${new LongBippy1(1L).withFoo(2L).foo ==== 2L}

  # Not foo
  @data class BarBippy1(bar: Int)
    can be constructed with "new"   ${new BarBippy1(1) must beAnInstanceOf[BarBippy1]}
    can be constructed with "apply" ${BarBippy1(1) ==== new BarBippy1(1)}
    has a copy method               ${new BarBippy1(1).copy(bar = 2).bar ==== 2}
    has a withBar method            ${new BarBippy1(1).withBar(2).bar ==== 2}

  # 0-arity
  @data class Bippy0()
    can be constructed with "new"   ${new Bippy0() must beAnInstanceOf[Bippy0]}
    has the correct toString        ${new Bippy0().toString ==== "Bippy0()"}
    two instances have the same ##  ${new Bippy0().## === new Bippy0().##}
    two instances are equal         ${new Bippy0 ==== new Bippy0}
    can be constructed with "apply" ${Bippy0() ==== new Bippy0}
    doesn't have a copy method      $bippy0NoCopy
    doesn't have a unapply method   $bippy0NoUnapply

  # 2-arity
  @data class Bippy2(foo: Int, bar: String)
    can be constructed with "new"   ${new Bippy2(1, "a") must beAnInstanceOf[Bippy2]}
    has the correct toString        ${new Bippy2(1, "a").toString ==== "Bippy2(1, a)"}
    two instances have the same ##  ${new Bippy2(1, "a").## ==== new Bippy2(1, "a").##}
    two instances are equal         ${new Bippy2(1, "a") ==== new Bippy2(1, "a")}
    can be constructed with "apply" ${Bippy2(1, "a") ==== new Bippy2(1, "a")}
    exposes its underlying type     ${new Bippy2(1, "a").foo ==== 1}
    exposes its underlying type     ${new Bippy2(1, "a").bar ==== "a"}
    has a copy method for foo       ${new Bippy2(1, "a").copy(foo = 2).foo ==== 2}
    has a copy method for bar       ${new Bippy2(1, "a").copy(bar = "b").bar ==== "b"}
    has a copy method for both      ${new Bippy2(1, "a").copy(foo = 2, bar = "b") ==== new Bippy2(2, "b")}
    has a withFoo method            ${new Bippy2(1, "a").withFoo(2).foo ==== 2}
    has a withBar method            ${new Bippy2(1, "a").withBar("b").bar ==== "b"}
    correct withFoo.withBar         ${new Bippy2(1, "a").withFoo(2).withBar("b") ==== new Bippy2(2, "b")}
    correct withBar.withFoo         ${new Bippy2(1, "a").withBar("b").withFoo(2) ==== new Bippy2(2, "b")}
    has an unapply                  $bippy2NoUnapply

  # 3-arity
  @data class Bippy3(foo: Int, bar: String, baz: Boolean)
    can be constructed with "new"   ${new Bippy3(1, "a", false) must beAnInstanceOf[Bippy3]}
    has the correct toString        ${new Bippy3(1, "a", false).toString ==== "Bippy3(1, a, false)"}
    two instances have the same ##  ${new Bippy3(1, "a", false).## ==== new Bippy3(1, "a", false).##}
    two instances are equal         ${new Bippy3(1, "a", false) ==== new Bippy3(1, "a", false)}
    can be constructed with "apply" ${Bippy3(1, "a", false) ==== new Bippy3(1, "a", false)}
    exposes its underlying type     ${new Bippy3(1, "a", false).foo ==== 1}
    exposes its underlying type     ${new Bippy3(1, "a", false).bar ==== "a"}
    exposes its underlying type     ${new Bippy3(1, "a", false).baz ==== false}
    has a copy method for foo       ${new Bippy3(1, "a", false).copy(foo = 2).foo ==== 2}
    has a copy method for bar       ${new Bippy3(1, "a", false).copy(bar = "b").bar ==== "b"}
    has a copy method for baz       ${new Bippy3(1, "a", false).copy(baz = true).baz ==== true}
    has a copy method for all       $bippy3CopyAll
    has a withFoo method            ${new Bippy3(1, "a", false).withFoo(2).foo ==== 2}
    has a withBar method            ${new Bippy3(1, "a", false).withBar("b").bar ==== "b"}
    has a withBaz method            ${new Bippy3(1, "a", false).withBaz(true).baz ==== true}
    correct chained withs           $bippy3ChainedWiths
    correct chained withs reverse   $bippy3ChainedWithsReverse
    has an unapply                  $bippy3NoUnapply

  # abstract class, class hierarchy
  @data abstract class AbstractBippy1(foo: Int)
  @data abstract class AbstractBippy2(override val foo: Int, bar: String) extends AbstractBippy1(foo)
  @data          class ConcreteBippy3(override val foo: Int, override val bar: String, baz: Boolean) extends AbstractBippy2(foo, bar)
    can be constructed with apply (1) ${ConcreteBippy3(1, "a", false) must beAnInstanceOf[ConcreteBippy3]}
    can be constructed with apply (2) ${ConcreteBippy3(1, "a", false) must beAnInstanceOf[AbstractBippy2]}
    can be constructed with apply (3) ${ConcreteBippy3(1, "a", false) must beAnInstanceOf[AbstractBippy1]}
    matches the 2-arity unapply       $concreteBippy3NoUnapply2

  # 1-arity -> 2-arity
  @data class Greeting(message: String, @since("0.2.0") date: java.util.Date = GreetingDate.date)
    can be constructed with just message with "new"   ${new Greeting("hi") must beAnInstanceOf[Greeting]}
    can be constructed with just message with "apply" ${Greeting("hi") must beAnInstanceOf[Greeting]}
    TODO: @since requires default value

  # 2-arity -> 3-arity
  @data class Person(name: String, surname: String, @since("0.2.0") dob: java.util.Date = GreetingDate.date)
    can be constructed with just message with "new"   ${new Person("john", "doe") must beAnInstanceOf[Person]}
    can be constructed with just message with "apply" ${Person("john", "doe") must beAnInstanceOf[Person]}
    can be deconstructed with 2-arity unapply         $personNoUnapply2
"""

  private def bippy1NoUnapply = (typecheck("Bippy1(1) match { case Bippy1(_) => 1 }")
    must failWith("object Bippy1 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def compBippy = (typecheck("@dataclasses.data class CompBippy(foo: Int); object CompBippy")
    pendingUntilFixed "https://github.com/scalameta/paradise/issues/12")

  private def bippy0NoCopy = (typecheck("Bippy0().copy()")
    must failWith("value copy is not a member of .*Bippy0") pendingUntilFixed "TODO")

  private def bippy0NoUnapply = (typecheck("Bippy0() match { case Bippy0() => 1 }")
    must failWith("object Bippy0 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def bippy2NoUnapply = (typecheck("""Bippy2(1, "a") match { case Bippy2(foo, bar) => 1 }""")
    must failWith("object Bippy2 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def bippy3CopyAll =
    new Bippy3(1, "a", false).copy(foo = 2, bar = "b", baz = true) ==== new Bippy3(2, "b", true)

  private def bippy3ChainedWiths =
    new Bippy3(1, "a", false).withFoo(2).withBar("b").withBaz(true) ==== new Bippy3(2, "b", true)

  private def bippy3ChainedWithsReverse =
    new Bippy3(1, "a", false).withBaz(true).withBar("b").withFoo(2) ==== new Bippy3(2, "b", true)

  private def bippy3NoUnapply = (
    typecheck("""Bippy3(1, "a", false) match { case Bippy3(foo, bar, baz) => 1 }""")
      must failWith("object Bippy3 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def concreteBippy3NoUnapply2 = (
    typecheck("""ConcreteBippy3(1, "a", false) match { case AbstractBippy2(foo, bar) => 1 }""")
      must failWith("object AbstractBippy2 is not a case class, nor does it have an unapply/unapplySeq member"))

  private def personNoUnapply2 = (
    typecheck("""Person("john", "doe") must beLike { case Person(n, sn) => n ==== "john" and sn ==== "doe" }""")
      must failWith("object Person is not a case class, nor does it have an unapply/unapplySeq member"))
}
