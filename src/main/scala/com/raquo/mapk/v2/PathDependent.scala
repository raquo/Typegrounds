package com.raquo.mapk.v2

import scala.language.implicitConversions

type Id[A] = A

// @TODO[WTF] It's bullshit that we need these Id[A] <> A conversions

given [A]: Conversion[A, Id[A]] = a => a

given [A]: Conversion[Id[A], A] = a => a

/** Kind[K]#T is equivalent to K[_] in Scala 2.
 *  You can express this with K[?] in Scala 3, but ONLY if K is a concrete type constructor, e.g. a class. I think.
 */
type Kind[K[_]] = {
  type A
  type T = K[A]
}

type KTuple2[T1[_], T2[_]] = {
  type A
  type T = (T1[A], T2[A])
}

type KTuple3[T1[_], T2[_], T3[_]] = {
  type A
  type T = (T1[A], T2[A], T3[A])
}


/** Used implicitly for MapK(fooKey -> Option[Foo], barKey -> Option[Bar]) syntax.
 *  @TODO[WTF] Needs ArrowAssoc to work implicitly.
 */
given KTuple2[T1[_], T2[_], A]: Conversion[(T1[A], T2[A]), KTuple2[T1, T2]#T] = _.asInstanceOf[KTuple2[T1, T2]#T]

/** Used implicitly for MapK(fooKey -> foo, barKey -> bar) syntax
 *  @TODO[WTF] Needs ArrowAssoc to work implicitly.
 */
given KTuple2_P1[K[_], A]: Conversion[(K[A], A), KTuple2[K, Id]#T] = _.asInstanceOf[KTuple2[K, Id]#T]

/** @TODO[WTF] Usually needs to be used explicitly. */
given KTuple3[T1[_], T2[_], T3[_], A]: Conversion[(T1[A], T2[A], T3[A]), KTuple3[T1, T2, T3]#T] = _.asInstanceOf[KTuple3[T1, T2, T3]#T]


// These don't seem to help remove the need for manual KTuple3 invocatio at all
//given KTuple3_P1[T1[_], A]: Conversion[(T1[A], A, A), KTuple3[T1, Id, Id]#T] = _.asInstanceOf[KTuple3[T1, Id, Id]#T]
//given KTuple3_P1_P2[T1[_], T2[_], A]: Conversion[(T1[A], T2[A], A), KTuple3[T1, T2, Id]#T] = _.asInstanceOf[KTuple3[T1, T2, Id]#T]



//implicit def wrapKTuple2[T1[_], T2[_], A](value: (T1[A], T2[A])): KTuple2[T1, T2]#T = value.asInstanceOf[KTuple2[T1, T2]#T]
//
//implicit def wrapKTuple2_P1[K[_], A](value: (K[A], A)): KTuple2[K, Id]#T = value.asInstanceOf[KTuple2[K, Id]#T]
//
//implicit def wrapKTuple3[T1[_], T2[_], T3[_], A](value: (T1[A], T2[A], T3[A])): KTuple3[T1, T2, T3]#T = value.asInstanceOf[KTuple3[T1, T2, T3]#T]
//
//implicit def wrapKTuple3_P1[T1[_], A](value: (T1[A], A, A)): KTuple3[T1, Id, Id]#T = value.asInstanceOf[KTuple3[T1, Id, Id]#T]
//
//implicit def wrapKTuple3_P1_P2[T1[_], T2[_], A](value: (T1[A], T2[A], A)): KTuple3[T1, T2, Id]#T = value.asInstanceOf[KTuple3[T1, T2, Id]#T]

//given unwrapKTuple2[T1[_], T2[_], A]:
//  Conversion[KTuple2[T1, T2]#T, (T1[A], T2[A])] = _.asInstanceOf[(T1[A], T2[A])]
//
//given unwrapKTuple3[T1[_], T2[_], T3[_], A]:
//  Conversion[KTuple3[T1, T2, T3]#T, (T1[A], T2[A], T3[A])] = _.asInstanceOf[(T1[A], T2[A], T3[A])]
//
//
//given unwrapKTuple2A[T1[_], T2[_], X]:
//  Conversion[(KTuple2[T1, T2] { type A = X })#A, X] = _.asInstanceOf[X]

//given wrapKTuple3_P1[T1[_], A]/*(using NotGiven[Conversion[(T1[A], Id[A], Id[A]), KTuple3[T1, Id, Id]#T]])*/:
//  Conversion[(T1[A], A, A), KTuple3[T1, Id, Id]#T] = _.asInstanceOf[KTuple3[T1, Id, Id]#T]
//
//given wrapKTuple3_P1_P2[T1[_], T2[_], A]/*(using NotGiven[Conversion[(T1[A], T2[A], Id[A]), KTuple3[T1, T2, Id]#T]])*/:
//  Conversion[(T1[A], T2[A], A), KTuple3[T1, T2, Id]#T] = _.asInstanceOf[KTuple3[T1, T2, Id]#T]


// (using NotGiven[Conversion[(T1[A], Id[A], Id[A]), KTuple3[T1, Id, Id]#T]])
