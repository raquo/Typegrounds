package com.raquo.mapk.v1

import com.raquo.generic.Key._
import com.raquo.mapk.v1.MapK
import com.raquo.generic.Key
import munit.FunSuite

class V1MapKSpec extends FunSuite {

  // Just test that this kind of signature will work
  def doSomething[A, K[_], V[_]](k: K[A], v: V[A]): Unit = {
    //println(s"$k -> $v")
  }

  val optionMap = MapK[Key, Option](boolKey -> Some(true), intKey -> Some(1), strKey -> Option("a"), strKey -> None)

  val idMap = MapK[Key, Id](boolKey -> true, intKey -> 1, strKey -> "hello")

  val expectedOptionValues = List[Type.Tuple3[Key, Option, Id]](
    (boolKey, Some(true), false),
    (intKey, Some(1), 0),
    (strKey, None, "")
  )

  val expectedIdValues = List[Type.Tuple3[Key, Id, Id]](
    (boolKey, true, false),
    (intKey, 1, 0),
    (strKey, "hello", "")
  )

  test("optionMap - apply & updated") {
    assertEquals(optionMap(intKey), Some(1))
    assertEquals(optionMap(strKey), None)
    assertEquals(optionMap.updated(strKey, Some("yo"))(strKey), Some("yo"))
  }

  test("optionMap - foreach") {
    var values: List[Type.Tuple3[Key, Option, Id]] = Nil

    optionMap.foreach { pair =>
      doSomething(pair._1, pair._2)
      doSomething(pair._1, pair._1.default)
      values = values :+ (pair._1, pair._2, pair._1.default)
    }

    assertEquals(values, expectedOptionValues)
  }

  test("optionMap - foreachK") {
    var values: List[Type.Tuple3[Key, Option, Id]] = Nil

    optionMap.foreachK { [A] => (k: Key[A], v: Option[A]) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ (k, v, k.default)
    }}

    assertEquals(values, expectedOptionValues)
  }

  test("idMap - apply & updated") {
    assertEquals(idMap(intKey), 1)
    assertEquals(idMap(strKey), "hello")
    assertEquals(idMap.updated(strKey, "yo")(strKey), "yo")
  }

  test("idMap - foreach") {
    var values: List[Type.Tuple3[Key, Id, Id]] = Nil

    idMap.foreach { pair =>
      doSomething(pair._1, pair._2)
      doSomething(pair._1, pair._1.default)
      values = values :+ (pair._1, pair._2, pair._1.default)
    }

    assertEquals(values, expectedIdValues)
  }

  test("idMap - foreachK") {
    var values: List[Type.Tuple3[Key, Id, Id]] = Nil

    idMap.foreachK {[A] => (k: Key[A], v: A) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ (k, v, k.default)
    }}

    assertEquals(values, expectedIdValues)
  }
}
