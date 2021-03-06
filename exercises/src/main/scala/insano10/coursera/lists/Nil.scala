package insano10.coursera.lists

object Nil extends List[Nothing] {

  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException("Nil.head") //Nothing is a subclass of all other classes so you can replace T with Nothing in the cases where an exception is thrown
                                                                   //to make it more explicit
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")

  override def toString = "."
}
