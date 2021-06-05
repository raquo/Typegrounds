package com.raquo.dependentTypes

import com.raquo.dependentTypes.Record
import com.raquo.dependentTypes.RecordType._
import com.raquo.dependentTypes.Record._

case class RecordKey[A](rt: RecordType, name: String, default: A) {
  type Rec = rt.Rec
}

object RecordKey {

  val AgeKey: RecordKey[Int] = RecordKey(PersonType, "age", 0)

  val NameKey: RecordKey[String] = RecordKey(PersonType, "name", "")

}
