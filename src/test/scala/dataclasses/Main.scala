package dataclasses

object Main {
  def main(args: Array[String]): Unit = {
    println(new Bippy1(1))
    println(new Bippy1(1).hashCode())
    println(new Bippy1(1).equals(new Bippy1(1)))
    println(new Bippy1(1).foo)
    println(new Bippy1(1).copy(foo = 2))
  }
}
