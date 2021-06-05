package com.raquo.dependentTypes

import com.raquo.dependentTypes.Record._

abstract class RecordType(name: String) {

  type Rec <: Record

  val default: Rec  // A bit ridiculous, but just testing the limits
}

object RecordType {

  // @TODO[WTF] Why does this not require `A <: Record`? Why does it fail if I specify that?
  type Kind[A] = RecordType { type Rec = A }

  case object PersonType extends RecordType("person") {
    override type Rec = Person
    override val default: Person = Person("Person")
  }

  case object DogType extends RecordType("dog") {
    override type Rec = Dog
    override val default: Dog = Dog("Dog")
  }

}
