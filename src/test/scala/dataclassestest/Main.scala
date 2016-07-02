package dataclassestest

import dataclasses._

@data class Bippy0()

@data class Bippy1(foo: Int)

@data class PrivateBippy1 private (foo: Int)
@data class ValBippy1(val foo: Int)
@data class LongBippy1(foo: Long)
@data class BarBippy1(bar: Int)

@data class Bippy2(foo: Int, bar: String)

@data class Bippy3(foo: Int, bar: String, baz: Boolean)

@data abstract class AbstractBippy1(foo: Int)
@data abstract class AbstractBippy2(override val foo: Int, bar: String) extends AbstractBippy1(foo)
@data          class ConcreteBippy3(override val foo: Int, override val bar: String, baz: Boolean) extends AbstractBippy2(foo, bar)

object GreetingDate {
  val date = {
    val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    df setTimeZone (java.util.TimeZone getTimeZone "UTC")
    df parse "2016-07-01T21:30:00.000Z"
  }
}

@data class Greeting(message: String, @since("0.2.0") date: java.util.Date = GreetingDate.date)

object Main {
  def main(args: Array[String]): Unit = {
    println(new Bippy0())
    println(new Bippy0().toString())
    println(new Bippy0().hashCode())
    println(new Bippy0().equals(new Bippy0()))
    println(Bippy0())

    println(new Bippy1(1))
    println(new Bippy1(1).foo)
    println(new Bippy1(1).toString())
    println(new Bippy1(1).hashCode())
    println(new Bippy1(1).equals(new Bippy1(1)))
    println(new Bippy1(1).copy(foo = 2))
    println(new Bippy1(1).withFoo(2))
    println(Bippy1(1))

    println(new LongBippy1(1L))
    println(new LongBippy1(1L).copy(foo = 2L))
    println(LongBippy1(1L))

    println(new BarBippy1(1))
    println(new BarBippy1(1).copy(bar = 2))
    println(new BarBippy1(1).withBar(2))
    println(BarBippy1(1))

    println(new Bippy2(1, "a"))
    println(new Bippy2(1, "a").foo)
    println(new Bippy2(1, "a").bar)
    println(new Bippy2(1, "a").toString())
    println(new Bippy2(1, "a").hashCode())
    println(new Bippy2(1, "a").equals(new Bippy2(1, "a")))
    println(new Bippy2(1, "a").copy(foo = 2))
    println(new Bippy2(1, "a").copy(bar = "b"))
    println(new Bippy2(1, "a").copy(foo = 2, bar = "b"))
    println(new Bippy2(1, "a").withFoo(2))
    println(new Bippy2(1, "a").withBar("b"))
    println(new Bippy2(1, "a").withFoo(2).withBar("b"))
    println(new Bippy2(1, "a").withBar("b").withFoo(2))
    println(Bippy2(1, "b"))
    println(Bippy2(1, "b") match { case Bippy2(foo, bar) => List(foo, bar) })

    println(new Bippy3(1, "a", false))
    println(new Bippy3(1, "a", false).foo)
    println(new Bippy3(1, "a", false).bar)
    println(new Bippy3(1, "a", false).baz)
    println(new Bippy3(1, "a", false).toString())
    println(new Bippy3(1, "a", false).hashCode())
    println(new Bippy3(1, "a", false).equals(new Bippy3(1, "a", false)))
    println(new Bippy3(1, "a", false).copy(foo = 2))
    println(new Bippy3(1, "a", false).copy(bar = "b"))
    println(new Bippy3(1, "a", false).copy(baz = true))
    println(new Bippy3(1, "a", false).copy(foo = 2, bar = "b", baz = true))
    println(new Bippy3(1, "a", false).withFoo(2))
    println(new Bippy3(1, "a", false).withBar("b"))
    println(new Bippy3(1, "a", false).withBaz(true))
    println(new Bippy3(1, "a", false).withFoo(2).withBar("b").withBaz(true))
    println(new Bippy3(1, "a", false).withBaz(true).withBar("b").withFoo(2))
    println(Bippy3(1, "b", false))
    println(Bippy3(1, "b", false) match { case Bippy3(foo, bar, baz) => List(foo, bar, baz) })

    println(ConcreteBippy3(1, "a", false))
    println(ConcreteBippy3(1, "a", false) match { case AbstractBippy2(foo, bar) => List(foo, bar) })

    println(new Greeting("hi"))
    println(Greeting("hi"))
  }
}
