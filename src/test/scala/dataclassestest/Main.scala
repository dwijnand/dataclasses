package dataclassestest

import dataclassesexamples._

object Main {
  def main(args: Array[String]): Unit = {
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
