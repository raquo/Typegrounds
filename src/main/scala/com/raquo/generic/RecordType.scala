package com.raquo.generic

import com.raquo.generic.Record._

abstract class RecordType[Rec <: Record](name: String) {
  val default: Rec // A bit ridiculous, but just testing the limits
}

object RecordType {

  case object PersonType extends RecordType[Person]("person") {
    override val default: Person = Person("Person")
  }

  case object DogType extends RecordType[Dog]("dog") {
    override val default: Dog = Dog("Dog")
  }

}
