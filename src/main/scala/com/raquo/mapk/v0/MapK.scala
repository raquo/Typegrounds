package com.raquo.mapk.v0

class MapK[K[_], V[_]] protected(protected val rawMap: Map[K[Any], V[Any]]) {

  def nonEmpty: Boolean = rawMap.nonEmpty

  def isEmpty: Boolean = rawMap.isEmpty

  def unsafeKeys: Iterable[K[Any]] = rawMap.keys.asInstanceOf[Iterable[K[Any]]]

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

  def +[A](pair: (K[A], V[A])): MapK[K, V] = updated(pair)

  def ++(otherMap: MapK[K, V]): MapK[K, V] = {
    var result = this
    otherMap.foreach { pair =>
      result = result.updated(pair.first, pair.second)
    }
    result
  }

  def -[A](key: K[A]): MapK[K, V] = {
    MapK.unsafeCoerce[K, V](rawMap - key.asInstanceOf[K[Any]])
  }

  def foldLeft[A](seed: A)(f: (A, Tuple2K[K, V, ?]) => A): A = {
    var acc = seed
    foreach { dataTuple =>
      acc = f(acc, dataTuple)
    }
    acc
  }

  def foldLeftK[Acc](seed: Acc)(f: [A] => (acc: Acc, key: K[A], value: V[A]) => Acc): Acc = {
    var acc = seed
    foreachK {
      [A] => (k: K[A], v: V[A]) =>
        acc = f(acc, key = k, value = v)
    }
    acc
  }

  def map[V2[_]](project: [A] => ((K[A], V[A])) => (K[A], V2[A])): MapK[K, V2] = {
    val newMap = rawMap.map(project.asInstanceOf[((K[Any], V[Any])) => (K[Any], V2[Any])])
    MapK.unsafeCoerce(newMap)
  }

  def mapValues[V2[_]](project: [A] => V[A] => V2[A]): MapK[K, V2] = {
    MapK.unsafeCoerce[K, V2](
      rawMap
        .view
        .mapValues[V2[Any]] { value =>
          project.asInstanceOf[V[Any] => V2[Any]](value)
        }
        .toMap
    )
  }

  def foreach(f: Tuple2K[K, V, ?] => Unit): Unit = {
    rawMap.foreach(pair => f(Tuple2K(pair._1, pair._2)))
  }

  def foreachK(f: [A] => (K[A], V[A]) => Unit): Unit = {
    foreach { pair => f(pair.first, pair.second) }
  }

  override def equals(obj: Any): Boolean = {
    // @TODO[Integrity] Be careful with subclasses here
    obj match {
      case mapK: MapK[_, _] => rawMap == mapK.rawMap
      case _ => false
    }
  }

  override def toString: String = {
    s"MapK(${rawMap.toString})"
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

  def apply[K[_], V[_]](entries: Tuple2K[K, V, ?]*): MapK[K, V] = {
    new MapK[K, V](Map(entries.map { pair =>
      (pair.first.asInstanceOf[K[Any]], pair.second.asInstanceOf[V[Any]])
    }: _*))
  }
}
