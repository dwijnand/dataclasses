package dataclasses

import scala.collection.immutable

final case class ProductImpl(override val productPrefix: String, elems: immutable.IndexedSeq[Any])
    extends Product {
  def productArity           = elems.size
  def productElement(n: Int) = elems(n)
}
