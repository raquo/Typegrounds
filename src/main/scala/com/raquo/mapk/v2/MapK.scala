package com.raquo.mapk.v2

class MapK[K[_], V[_]](rawMap: Map[K[Any], V[Any]]) {

  def nonEmpty: Boolean = rawMap.nonEmpty

  def isEmpty: Boolean = rawMap.isEmpty

  def keys: Iterable[Kind[K]#T] = rawMap.keys.asInstanceOf[Iterable[Kind[K]#T]]

  def contains[A](key: K[A]): Boolean = {
    rawMap.contains(key.asInstanceOf[K[Any]])
  }

  def apply[A](key: K[A]): V[A] = {
    rawMap(key.asInstanceOf[K[Any]]).asInstanceOf[V[A]]
  }

  def get[A](key: K[A]): Option[V[A]] = {
    rawMap.get(key.asInstanceOf[K[Any]]).asInstanceOf[Option[V[A]]]
  }

  def size: Int = rawMap.size

  def updated[A](key: K[A], value: V[A]): MapK[K, V] = {
    MapK.unsafeCoerce(rawMap.updated(key.asInstanceOf[K[Any]], value.asInstanceOf[V[Any]]))
  }

  def updated[A](pair: (K[A], V[A])): MapK[K, V] = {
    MapK.unsafeCoerce(rawMap.updated(pair._1.asInstanceOf[K[Any]], pair._2.asInstanceOf[V[Any]]))
  }

  /** #Unsafe: `f` must NOT rely on `A` being any particular type */
  private def unsafeForeach[A](f: ((K[A], V[A])) => Unit): Unit = {
    rawMap.foreach(pair => f(pair.asInstanceOf[(K[A], V[A])]))
  }

  def foreach(f: KTuple2[K, V]#T => Unit): Unit = {
    unsafeForeach( (k, v) => f((k, v).asInstanceOf[KTuple2[K, V]#T]))
  }

  def foreachK(f: [A] => (K[A], V[A]) => Unit): Unit = {
    unsafeForeach((k, v) => f(k, v))
  }
}

object MapK {

  /** #Unsafe: assumes that `rawMap` is a valid source for MapK,
   *   i.e. that every (K[Any], V[Any]) pair in `rawMap` is actually `(K[A], V[A]) forSome { type A }`.
   *   For extra clarity, different pairs in `rawMap` can have different `A` types,
   *   it must only be consistent within the same key-value pair.
   */
  def unsafeCoerce[K[_], V[_]](rawMap: Map[K[Any], V[Any]]): MapK[K, V] = {
    new MapK[K, V](rawMap)
  }

  def empty[K[_], V[_]]: MapK[K, V] = MapK()

  def apply[K[_], V[_]](pairs: KTuple2[K, V]#T*): MapK[K, V] = {
    val x: List[KTuple2[K, V]#T] = pairs.toList
    val y: List[(K[Any], V[Any])] = x.map(t => t.asInstanceOf[(K[Any], V[Any])])
    new MapK(Map(y: _*))
  }

}
