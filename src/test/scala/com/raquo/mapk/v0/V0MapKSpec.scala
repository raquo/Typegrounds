package com.raquo.mapk.v0

import com.raquo.generic.Key._
import com.raquo.mapk.v0.MapK
import com.raquo.generic.Key
import munit.FunSuite

import scala.language.implicitConversions

class V0MapKSpec extends FunSuite {

  // Just test that this kind of signature will work
  def doSomething[A, K[_], V[_]](k: K[A], v: V[A]): Unit = {
    //println(s"$k -> $v")
  }

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

  val expectedOptionValues = List[Tuple3K[Key, Option, Id, ?]](
    Tuple3K(boolKey, Some(true), false),
    Tuple3K(intKey, Some(1), 0),
    Tuple3K(strKey, None, "")
  )

  val expectedIdValues = List[Tuple3K[Key, Id, Id, ?]](
    Tuple3K(boolKey, true, false),
    Tuple3K(intKey, 1, 0),
    Tuple3K(strKey, "hello", "")
  )

  test("optionMap - apply & updated") {
    assertEquals(optionMap(intKey), Some(1))
    assertEquals(optionMap(strKey), None)
    assertEquals(optionMap.updated(strKey, Some("yo"))(strKey), Some("yo"))
  }

  test("optionMap - foreach") {
    var values: List[Tuple3K[Key, Option, Id, ?]] = Nil

    optionMap.foreach { pair =>
      doSomething(pair.first, pair.second)
      doSomething(pair.first, pair.first.default)
      values = values :+ (pair.first, pair.second, pair.first.default)
    }

    assertEquals(values, expectedOptionValues)
  }

  test("optionMap - foreachK") {
    var values: List[Tuple3K[Key, Option, Id, ?]] = Nil

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
    var values: List[Tuple3K[Key, Id, Id, ?]] = Nil

    idMap.foreach { pair =>
      doSomething(pair.first, pair.second)
      doSomething(pair.first, pair.first.default)
      values = values :+ (pair.first, pair.second, pair.first.default)
    }

    assertEquals(values, expectedIdValues)
  }

  test("idMap - foreachK") {
    var values: List[Tuple3K[Key, Id, Id, ?]] = Nil

    idMap.foreachK {[A] => (k: Key[A], v: A) => {
      doSomething(k, v)
      doSomething(k, k.default)
      values = values :+ (k, v, k.default)
    }}

    assertEquals(values, expectedIdValues)
  }
}
