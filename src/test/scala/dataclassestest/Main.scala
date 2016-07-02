package dataclassestest

import dataclassesexamples._

object Main {
  def main(args: Array[String]): Unit = {
    println(ConcreteBippy3(1, "a", false))
    println(ConcreteBippy3(1, "a", false) match { case AbstractBippy2(foo, bar) => List(foo, bar) })

    println(new Greeting("hi"))
    println(Greeting("hi"))
  }
}
