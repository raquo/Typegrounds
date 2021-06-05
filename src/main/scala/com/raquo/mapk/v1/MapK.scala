package com.raquo.mapk.v1

class MapK[K[_], V[_]] protected(protected val rawMap: Map[Type[K], Type[V]]) {

  def nonEmpty: Boolean = rawMap.nonEmpty

  def isEmpty: Boolean = rawMap.isEmpty

  def keys: Iterable[Type[K]] = rawMap.keys.asInstanceOf[Iterable[Type[K]]]

  def contains[A](key: K[A]): Boolean = {
    rawMap.contains(key)
  }

  def apply[A](key: K[A]): V[A] = {
    rawMap(key).asInstanceOf[V[A]]
  }

  def get[A](key: K[A]): Option[V[A]] = {
    rawMap.get(key).asInstanceOf[Option[V[A]]]
  }

  def size: Int = rawMap.size

  def updated[A](key: K[A], value: V[A]): MapK[K, V] = {
    MapK.unsafeCoerce(rawMap.updated(key, value))
  }

  def updated[A](pair: (K[A], V[A])): MapK[K, V] = {
    MapK.unsafeCoerce(rawMap.updated(pair._1, pair._2))
  }

  def +[A](pair: (K[A], V[A])): MapK[K, V] = updated(pair)

  def ++(otherMap: MapK[K, V]): MapK[K, V] = {
    var result = this
    otherMap.foreach { pair =>
      result = result.updated(pair._1, pair._2)
    }
    result
  }

  def -[A](key: K[A]): MapK[K, V] = {
    MapK.unsafeCoerce(rawMap - key)
  }

  def foldLeft[A](seed: A)(f: (A, Type.Tuple2[K, V]) => A): A = {
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
    val newMap = rawMap.map(project.asInstanceOf[((Type[K], Type[V])) => (Type[K], Type[V2])])
    MapK.unsafeCoerce(newMap)
  }

  def mapValues[V2[_]](project: [A] => V[A] => V2[A]): MapK[K, V2] = {
    MapK.unsafeCoerce[K, V2](
      rawMap
        .view
        .mapValues[Type[V2]] { value =>
          val v = unwrap(value)
          project(v)
        }
        .toMap
    )
  }

  // #Unsafe – user can provide any A, which is unsound.
  //private def unsafeForeach[A](f: ((K[A], V[A])) => Unit): Unit = {
  //  rawMap.foreach(f.asInstanceOf[((Type[K], Type[V])) => Any])
  //}

  def foreach(f: Type.Tuple2[K, V] => Unit): Unit = {
    rawMap.foreach(f.asInstanceOf[((Type[K], Type[V])) => Any])
    //unsafeForeach { (k, v) => f((k, v)) }
  }

  def foreachK(f: [A] => (K[A], V[A]) => Unit): Unit = {
    foreach { pair => f(pair._1, pair._2) }
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
   * i.e. that every (K[?], V[?]) pair in `rawMap` is actually `(K[A], V[A]) forSome { type A }`.
   * For extra clarity, different pairs in `rawMap` can have different `A` types,
   * it must only be consistent within the same key-value pair.
   */
  def unsafeCoerce[K[_], V[_]](rawMap: Map[Type[K], Type[V]]): MapK[K, V] = {
    new MapK[K, V](rawMap)
  }

  def empty[K[_], V[_]]: MapK[K, V] = MapK()

  def apply[K[_], V[_]](entries: Type.Tuple2[K, V]*): MapK[K, V] = {
    new MapK[K, V](Map(entries.asInstanceOf[Seq[(Type[K], Type[V])]]: _*))
  }
}
