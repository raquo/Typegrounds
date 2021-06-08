package com.raquo.mapk.v1

import scala.language.implicitConversions

type Id[A] = A

implicit def wrapId[A](a: A): Id[A] = a

//implicit def unwrapId[A](a: Id[A]): A = a


type Type[F[_]] <: (Any { type T })


object Type {

  type Tuple2[F[_], G[_]] <: (Any { type T })

  type Tuple3[F[_], G[_], H[_]] <: (Any { type T })

  // type Function2[F[_], G[_], Out] <: (Any { type T })
}

// Scala 2 style implicits, but whatever, conversion to Scala 3 style is straightforward, see PathDependent.scala

implicit def wrap[F[_], A](value: F[A]): Type[F] =
  value.asInstanceOf[Type[F]]

implicit def wrapT2[F[_], G[_], A](value: (F[A], G[A])): Type.Tuple2[F, G] =
  value.asInstanceOf[Type.Tuple2[F, G]]

//implicit def wrapF2[F[_], G[_], Out](value: (Type[F], Type[G]) => Out): Type.Function2[F, G, Out] =
//  value.asInstanceOf[Type.Function2[F, G, Out]]

implicit def wrapT2_P1[F[_], A](t: (F[A], A)): Type.Tuple2[F, Id] = wrapT2[F, Id, A](t)

implicit def wrapT3[F[_], G[_], H[_], A](value: (F[A], G[A], H[A])): Type.Tuple3[F, G, H] =
  value.asInstanceOf[Type.Tuple3[F, G, H]]

implicit def wrapT3_P1[F[_], G[_], A](value: (F[A], A, A)): Type.Tuple3[F, Id, Id] =
  value.asInstanceOf[Type.Tuple3[F, Id, Id]]

implicit def wrapT3_P1_P2[F[_], G[_], A](value: (F[A], G[A], A)): Type.Tuple3[F, G, Id] =
  value.asInstanceOf[Type.Tuple3[F, G, Id]]


implicit def unwrap[F[_]](value: Type[F]): F[value.T] =
  value.asInstanceOf[F[value.T]]

implicit def unwrapT2[F[_], G[_]](value: Type.Tuple2[F, G]): (F[value.T], G[value.T]) =
  value.asInstanceOf[(F[value.T], G[value.T])]

implicit def unwrapT3[F[_], G[_], H[_]](value: Type.Tuple3[F, G, H]): (F[value.T], G[value.T], H[value.T]) =
  value.asInstanceOf[(F[value.T], G[value.T], H[value.T])]

