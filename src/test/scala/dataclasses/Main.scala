package dataclasses

@data class Bippy1(foo: Int)

@data class PrivateBippy1 private (foo: Int)
@data class ValBippy1(val foo: Int)
@data class LongBippy1(foo: Long)
@data class BarBippy1(bar: Int)

//@data class Bippy2(foo: Int, bar: String)

//@data class Bippy3(foo: Int, bar: String, baz: Boolean)


object Main {
  def main(args: Array[String]): Unit = {
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
  }
}
