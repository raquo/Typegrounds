package com.raquo.dependentTypes

trait Record

object Record {

  case class Person(name: String) extends Record

  case class Dog(name: String) extends Record

}
