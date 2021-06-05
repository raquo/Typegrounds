package com.raquo.generic

import com.raquo.generic.Record
import com.raquo.generic.RecordType._
import com.raquo.generic.Record._

case class RecordKey[Rec <: Record, A](rt: RecordType[Rec], name: String)

object RecordKey {

  type Kind = {
    type Rec <: Record
    type T = RecordType[Rec]
  }

  val AgeKey: RecordKey[Person, Int] = RecordKey(PersonType, "age")

  val NameKey: RecordKey[Person, String] = RecordKey(PersonType, "name")

}
