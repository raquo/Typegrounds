package com.raquo.generic

case class Key[A](label: String, default: A)

object Key {

  val boolKey = Key[Boolean]("bool", false)

  val intKey = Key[Int]("int", 0)

  val strKey = Key[String]("str", "")

}