package dataclassesexamples

import dataclasses._

@data class Bippy1(foo: Int)
@data class LongBippy1(foo: Long)
@data class BarBippy1(bar: Int)
@data class Bippy0()
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

@data class Person(name: String, surname: String, @since("0.2.0") dob: java.util.Date = GreetingDate.date)
