package com.raquo.mapk.v0

type Id[A] = A

given wrapId[A]: Conversion[A, Id[A]] = a => a


trait FunctionK[F[_], G[_]] {

  def apply[A](fa: F[A]): G[A]
}


case class Tuple2K[T1[_], T2[_], A](first: T1[A], second: T2[A])

case class Tuple3K[T1[_], T2[_], T3[_], A](first: T1[A], second: T2[A], third: T3[A])

extension [K[_], A] (key: K[A])
  def ->[V[_]](value: V[A]): Tuple2K[K, V, A] = Tuple2K(key, value)

//given wrapTuple2K[T1[_], T2[_], A]: Conversion[(T1[A], T2[A]), Tuple2K[T1, T2, A]] = (t1, t2) => Tuple2K(t1, t2)

//given wrapTuple2K_P1[T1[_], A]: Conversion[(T1[A], Id[A]), Tuple2K[T1, Id, A]] = (t1, t2) => Tuple2K(t1, t2)

given wrapTuple3K[T1[_], T2[_], T3[_], A]: Conversion[(T1[A], T2[A], T3[A]), Tuple3K[T1, T2, T3, A]] = (t1, t2, t3) => Tuple3K(t1, t2, t3)

given wrapTuple3K_P1_P2[T1[_], T2[_], A]: Conversion[(T1[A], T2[A], Id[A]), Tuple3K[T1, T2, Id, A]] = (t1, t2, t3) => Tuple3K(t1, t2, t3)

given wrapTuple3K_P1_P3[T1[_], T3[_], A]: Conversion[(T1[A], Id[A], T3[A]), Tuple3K[T1, Id, T3, A]] = (t1, t2, t3) => Tuple3K(t1, t2, t3)

given wrapTuple3K_P1[T1[_], A]: Conversion[(T1[A], Id[A], Id[A]), Tuple3K[T1, Id, Id, A]] = (t1, t2, t3) => Tuple3K(t1, t2, t3)
