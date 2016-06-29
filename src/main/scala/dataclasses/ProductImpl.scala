package dataclasses

final case class ProductImpl(override val productPrefix: String, elems: IndexedSeq[Any]) extends Product {
  def productArity           = elems.size
  def productElement(n: Int) = elems(n)
}
