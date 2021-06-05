package com.raquo.mapk.v2

import com.raquo.dependentTypes.Record._
import com.raquo.dependentTypes.RecordType
import com.raquo.dependentTypes.RecordType.{DogType, PersonType}
import com.raquo.dependentTypes.RecordKey
import com.raquo.dependentTypes.RecordKey._
import com.raquo.generic.Key._
import com.raquo.generic.Key
import munit.FunSuite

import scala.language.implicitConversions

class V2MapKSpec extends FunSuite {

  // Just test that this kind of signature will work
  def doSomething[A, K[_], V[_]](k: K[A], v: V[A]): Unit = {
    //println(s"$k -> $v")
  }

  // @TODO[WTF] Somehow, ArrowAssoc makes the given instance work in this case
  val optionMap = MapK[Key, Option](
    boolKey -> Some(true),
    intKey -> Some(1),
    strKey -> Option("a"),
    strKey -> None
  )

  val idMap = MapK[Key, Id](
    boolKey -> true,
    intKey -> 1,
    strKey -> "hello"
  )

  val recMap = MapK[RecordKey, Id](
    AgeKey -> 1,
    NameKey -> "a"
  )

  // @TODO Is it possible to get rid of KTuple2 here?
  val rtMap = MapK[RecordType.Kind, Id](
    KTuple2(PersonType -> Person("Alex")),
    KTuple2(DogType -> Dog("Bark"))
  )

  // @TODO[WTF] We don't use ArrowAssoc here, so the given instance is not found, we need to manually invoke it
  val expectedOptionValues = List[KTuple3[Key, Option, Id]#T](
    KTuple3(boolKey, Some(true), false),
    KTuple3(intKey, Some(1), 0),
    KTuple3(strKey, None, "")
  )

  val expectedIdValues = List[KTuple3[Key, Id, Id]#T](
    KTuple3(boolKey, true, false),
    KTuple3(intKey, 1, 0),
    KTuple3(strKey, "hello", "")
  )

  val expectedRecValues = List[KTuple3[RecordKey, Id, Id]#T](
    KTuple3(AgeKey, 1, 0),
    KTuple3(NameKey, "a", "")
  )

  val expectedRtValues = List[KTuple3[RecordType.Kind, Id, Id]#T](
    KTuple3(PersonType, Person("Alex"), Person("Person")),
    KTuple3(DogType, Dog("Bark"), Dog("Dog"))
  )

  test("optionMap - apply & updated") {
    assertEquals(optionMap(intKey), Some(1))
    assertEquals(optionMap(strKey), None)
    assertEquals(optionMap.updated(strKey, Some("yo"))(strKey), Some("yo"))
  }

  test("optionMap - foreach") {
    var values: List[KTuple3[Key, Option, Id]#T] = Nil

    optionMap.foreach { (k, v) =>
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3(k, v, k.default)
    }

    assertEquals(values, expectedOptionValues)
  }

  test("optionMap - foreachK") {
    var values: List[KTuple3[Key, Option, Id]#T] = Nil

    optionMap.foreachK { [A] => (k: Key[A], v: Option[A]) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3(k, v, k.default)
    }}

    assertEquals(values, expectedOptionValues)
  }

  test("idMap - apply & updated") {
    assertEquals(idMap(intKey), 1)
    assertEquals(idMap(strKey), "hello")
    assertEquals(idMap.updated(strKey, "yo")(strKey), "yo")
  }

  test("idMap - foreach") {
    var values: List[KTuple3[Key, Id, Id]#T] = Nil

    idMap.foreach { (k, v) =>
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3((k, v, k.default))
    }

    assertEquals(values, expectedIdValues)
  }

  test("idMap - foreachK") {
    var values: List[KTuple3[Key, Id, Id]#T] = Nil

    idMap.foreachK { [A] => (k: Key[A], v: A) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3(k, v, k.default)
    }}

    assertEquals(values, expectedIdValues)
  }

  test("recMap - apply & updated") {
    assertEquals(recMap(AgeKey), 1)
    assertEquals(recMap(NameKey), "a")
    assertEquals(recMap.updated(NameKey, "yo")(NameKey), "yo")
  }

  test("recMap - foreach") {
    var values: List[KTuple3[RecordKey, Id, Id]#T] = Nil

    recMap.foreach { (k, v) =>
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3((k, v, k.default))
    }

    assertEquals(values, expectedRecValues)
  }

  test("recMap - foreachK") {
    var values: List[KTuple3[RecordKey, Id, Id]#T] = Nil

    recMap.foreachK { [A] => (k: RecordKey[A], v: A) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3(k, v, k.default)
    }}

    assertEquals(values, expectedRecValues)
  }

  // --

  test("rtMap - apply & updated") {
    assertEquals(rtMap(PersonType), Person("Alex"))
    assertEquals(rtMap(DogType), Dog("Bark"))
    assertEquals(rtMap.updated(DogType, Dog("Bark II"))(DogType), Dog("Bark II"))
  }

  test("rtMap - foreach") {
    var values: List[KTuple3[RecordType.Kind, Id, Id]#T] = Nil

    rtMap.foreach { (k, v) =>
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3((k, v, k.default))
    }

    assertEquals(values, expectedRtValues)
  }

  test("rtMap - foreachK") {
    var values: List[KTuple3[RecordType.Kind, Id, Id]#T] = Nil

    rtMap.foreachK { [A] => (k: RecordType { type Rec = A }, v: A) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ KTuple3(k, v, k.default)
    }}

    assertEquals(values, expectedRtValues)
  }
}
